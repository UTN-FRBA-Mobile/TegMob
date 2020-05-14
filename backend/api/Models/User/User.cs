using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace api.Models.User
{
	public class User
	{
		[BsonId]
		[BsonRepresentation(BsonType.ObjectId)]
		public string Id { get; set; }
		public string FirstName { get; set; }
		public string LastName { get; set; }

		public string Username { get; set; }

		[JsonIgnore]
		public byte[] PasswordHash { get; set; }
		[JsonIgnore]
		public byte[] PasswordSalt { get; set; }

		public string Token { get; set; }
	}
}
