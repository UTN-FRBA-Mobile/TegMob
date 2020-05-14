﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace api.Models.User
{
	public class UserRegisterModel
	{
		[Required]
		public string FirstName { get; set; }
		[Required]
		public string LastName { get; set; }

		[Required]
		public string Username { get; set; }
		[Required]
		public string Password { get; set; }
	}
}
