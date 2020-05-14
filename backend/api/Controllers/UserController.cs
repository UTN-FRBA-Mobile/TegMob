using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using api.Helpers;
using api.Models.User;
using api.Services;
using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace api.Controllers
{
	[Authorize]
	[ApiController]
	[Route("[controller]")]
	public class UserController : ControllerBase
	{
		private readonly IUserService _userService;
		private readonly IMapper _mapper;
		public UserController(IUserService userService, IMapper mapper)
		{
			_userService = userService;
			_mapper = mapper;
		}

		[AllowAnonymous]
		[HttpPost("authenticate")]
		public IActionResult Authenticate([FromBody]UserAuthenticateModel userAuthenticateModel)
		{
			User user = _userService.Authenticate(userAuthenticateModel.Username, userAuthenticateModel.Password);
			if (user == null) return NotFound();

			UserModel model = _mapper.Map<UserModel>(user);

			return Ok(model);
		}

		[AllowAnonymous]
		[HttpPost("register")]
		public IActionResult Register([FromBody]UserRegisterModel userRegisterModel)
		{
			User user = _mapper.Map<User>(userRegisterModel);

			try
			{
				User temp = _userService.Create(user, userRegisterModel.Password);
				UserModel model = _mapper.Map<UserModel>(temp);

				return Ok(model);
			}
			catch (AppException ex)
			{
				return BadRequest(new { message = ex.Message });
			}
		}

		[HttpGet]
		public IActionResult Get()
		{
			IEnumerable<User> users = _userService.GetAll();
			IEnumerable<UserModel> model = _mapper.Map<IEnumerable<UserModel>>(users);
			return Ok(model);
		}
	}
}
