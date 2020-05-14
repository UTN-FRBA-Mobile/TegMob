using api.Models.User;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace api.Services
{
	public interface IUserService
	{
		User Authenticate(string username, string password);
		IEnumerable<User> GetAll();
		User Create(User user, string password);
	}
}
