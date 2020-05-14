namespace api.Helpers
{
	public interface IAppSettings
	{
		public string JWTSecret { get; set; }
		public DBConnect DBConnect { get; set; }

	}
	public class AppSettings : IAppSettings
	{
		public string JWTSecret { get; set; }
		public DBConnect DBConnect { get; set; }
	}
}
