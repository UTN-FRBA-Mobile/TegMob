using AutoMapper;
using api.Models.User;

namespace api.Helpers
{
	public class AutoMapperProfile : Profile
	{
		public AutoMapperProfile()
		{
			CreateMap<User, UserModel>();
			CreateMap<UserRegisterModel, User>();
		}
	}
}
