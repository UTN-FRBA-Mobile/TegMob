using api.Helpers;
using api.Models.User;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using MongoDB.Driver;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Linq.Expressions;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace api.Services
{
	public class UserMongoService : IUserService
	{
		private readonly IAppSettings _appSettings;
		private readonly IMongoCollection<User> _users;
		public UserMongoService(IOptions<AppSettings> appSettings)
		{
			_appSettings = appSettings.Value;
			MongoClient client = new MongoClient(_appSettings.DBConnect.ConnectionString);
			IMongoDatabase database = client.GetDatabase(_appSettings.DBConnect.DatabaseName);

			_users = database.GetCollection<User>("users");
		}
		public User Authenticate(string username, string password)
		{
			if (string.IsNullOrWhiteSpace(username) || string.IsNullOrWhiteSpace(password))
				throw new ArgumentException("Password can't be empty.", "password");

			User user = _users.Find(u => u.Username == username).FirstOrDefault();

			if (user == null) return null;

			if (!Utils.VerifyPasswordHash(password, user.PasswordHash, user.PasswordSalt)) return null;

			JwtSecurityTokenHandler tokenHandler = new JwtSecurityTokenHandler();
			byte[] key = Encoding.ASCII.GetBytes(_appSettings.JWTSecret);
			SecurityTokenDescriptor tokenDescriptor = new SecurityTokenDescriptor
			{
				Subject = new ClaimsIdentity(new Claim[]
				{
					new Claim(ClaimTypes.Name, user.Id)
				}),
				Expires = DateTime.UtcNow.AddDays(7),
				SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
			};
			SecurityToken token = tokenHandler.CreateToken(tokenDescriptor);
			user.Token = tokenHandler.WriteToken(token);

			_users.ReplaceOne<User>(u => u.Username == username, user);

			return user;
		}

		public User Create(User user, string password)
		{
			if (string.IsNullOrWhiteSpace(password))
				throw new AppException("Password is required");

			if (_users.Find(u => u.Username == user.Username).FirstOrDefault() != null)
				throw new AppException("Username \"" + user.Username + "\" is already taken");

			byte[] passwordHash, passwordSalt;
			Utils.CreatePasswordHash(password, out passwordHash, out passwordSalt);

			user.PasswordHash = passwordHash;
			user.PasswordSalt = passwordSalt;

			_users.InsertOne(user);

			return _users.Find(u => u.Username == user.Username).FirstOrDefault();
		}

		public IEnumerable<User> GetAll()
		{
			List<User> users = _users.Find<User>(u => true).ToList();
			return users;
		}
	}
}
