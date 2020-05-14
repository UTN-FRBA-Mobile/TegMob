using System;
using System.Text;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using api.Helpers;
using api.Services;
using AutoMapper;

namespace api
{
	public class Startup
	{
		public Startup(IConfiguration configuration)
		{
			Configuration = configuration;
		}

		public IConfiguration Configuration { get; }

		// This method gets called by the runtime. Use this method to add services to the container.
		public void ConfigureServices(IServiceCollection services)
		{
			services.AddCors();

			IConfigurationSection appSettingsSection = Configuration.GetSection("AppSettings");
			services.Configure<AppSettings>(appSettingsSection);
			
			services.AddControllers();

			services.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());

			AppSettings appSettings = appSettingsSection.Get<AppSettings>();
			var jwtKey = Encoding.ASCII.GetBytes(appSettings.JWTSecret);

			services.AddAuthentication(ao =>
			{
				ao.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
				ao.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
			}).AddJwtBearer(jbo =>
			{
				jbo.RequireHttpsMetadata = false;
				jbo.SaveToken = true;
				jbo.TokenValidationParameters = new TokenValidationParameters
				{
					ValidateIssuerSigningKey = true,
					IssuerSigningKey = new SymmetricSecurityKey(jwtKey),
					ValidateIssuer = false,
					ValidateAudience = false
				};
			});

			services.AddScoped<IUserService, UserMongoService>();
		}

		// This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
		public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
		{
			if (env.IsDevelopment())
			{
				app.UseDeveloperExceptionPage();
			}

			app.UseHttpsRedirection();

			app.UseRouting();

			app.UseCors(cpb => cpb
				.AllowAnyOrigin()
				.AllowAnyMethod()
				.AllowAnyHeader());

			app.UseAuthentication();
			app.UseAuthorization();

			app.UseEndpoints(endpoints =>
			{
				endpoints.MapControllers();
			});
		}
	}
}
