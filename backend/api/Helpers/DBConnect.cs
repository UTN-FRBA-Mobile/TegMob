namespace api.Helpers
{
	public interface IDBConnect
	{
		public string ConnectionString { get; set; }
		public string DatabaseName { get; set; }
	}
	public class DBConnect
	{
		public string ConnectionString { get; set; }
		public string DatabaseName { get; set; }
	}
}
