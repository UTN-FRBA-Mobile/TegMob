using AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using tegmobapi.Models.User;

namespace tegmobapi.Helpers
{
	public class AutoMapperProfile : Profile
	{
		public AutoMapperProfile()
		{
			CreateMap<User, UserModel>();
			CreateMap<UserRegisterModel, User>();
			CreateMap<UserUpdateModel, User>();
		}
	}
}
