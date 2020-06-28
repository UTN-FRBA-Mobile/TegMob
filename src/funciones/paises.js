// Importar para ejecutar funciones: var tools = require('<archivo>');
// ejecute: tools.<funcion>(<variable>);

module.exports = {

	// Obtener los limitrofes, solo los disponibles en la APP
	get_limitrofes: function (pais) {
		// Chequear si el pais realmente esta dentro de los utilizados.
		if (this.es_utilizado(pais)) {
			var limitrofes = [];
			paises_limitrofes[pais].forEach(element => {
				if (this.es_utilizado(element)) {
					limitrofes.push(element);
				}
			});
			return limitrofes;
		} else {
			return 'array vacio';
		}
	},

	// Paises que declaramos estan dispobles en la APP
	es_utilizado: function (pais) {
		return (paises_usados_por_nosotros.includes(pais));
	},

	// Crea el mapa inicial, recibe lista de colores
	// n: numero de colores
	// c_list: lista de colores pasada por parametro
	getMapaInicial: function (c_list) {
		var mapa_ini = {}
		var colores = c_list.slice()
		var rondas_extra = 2

		this.shuffleArray(paises_usados_por_nosotros).slice().forEach(pais => {
			mapa_ini[pais] = { 'owner': colores.pop() }
			if (rondas_extra > 0) // paises con mas ejercito para atacar
				mapa_ini[pais].armies = 5;
			else
				mapa_ini[pais].armies = 1;
			if (colores.length < 1) {
				colores = c_list.slice()
				rondas_extra -= 1
			}
		});
		return mapa_ini;
	},

	shuffleArray: function (array) {
		for (var i = array.length - 1; i > 0; i--) {
			var j = Math.floor(Math.random() * (i + 1));
			var temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
		return array;
	},

	// Colores disponibles
	getNColoresPosibles: function (n = 6) {
		return colores.slice(0, n)
	}

}

// Colores posibles
var colores = [
	"green",
	"yellow",
	"black",
	"red",
	"magenta",
	"cyan"
]

// Actualizar segun tengamos disponibles en el mapa.
// Case sensitive.
var paises_usados_por_nosotros = [
	'brazil',
	'colombia',
	'chile',
	'peru',
	'argentina',
	'uruguay',
	'egypt',
	'ethiopia',
	'zaire',
	'madagascar',
	'southafrica',
	'sahara'
];

var paises_por_continente = {
	'Africa': ['egypt', 'ethiopia', 'madagascar', 'sahara', 'southafrica', 'zaire'],
	'America del Sur': ['argentina', 'brazil', 'chile', 'colombia', 'peru', 'uruguay'],
	'America del Norte': ['Alaska', 'California', 'Canada', 'Groenlandia', 'Labrador', 'Nueva York', 'Mexico', 'Oregon', 'Terranova', 'Yukon'],
	'Asia': ['Arabia', 'Aral', 'China', 'Gobi', 'India', 'Iran', 'Israel', 'Japon', 'Kamchatka', 'Malasia', 'Mongolia', 'Siberia', 'Taimir', 'Tartaria', 'Turquia'],
	'Europa': ['Alemania', 'Espana', 'Francia', 'Gran Bretana', 'Islandia', 'Italia', 'Polonia', 'Rusia', 'Suecia'],
	'Oceania': ['Australia', 'Borneo', 'Java', 'Sumatra'],
}

// actualizar
var paises_limitrofes = {
	'Alaska': ['Kamchatka', 'Oregon', 'Yukon'],
	'Alemania': ['Francia', 'Gran Bretana', 'Italia', 'Polonia'],
	'Arabia': ['Israel', 'Turquia'],
	'Aral': ['Iran', 'Mongolia', 'Rusia', 'Siberia', 'Tartaria'],
	'argentina': ['brazil', 'chile', 'peru', 'uruguay'],
	'Australia': ['Borneo', 'chile', 'Java', 'Sumatra'],
	'Borneo': ['Australia', 'Malasia'],
	'brazil': ['argentina', 'colombia', 'peru', 'sahara', 'uruguay'],
	'California': ['Mexico', 'Nueva York', 'Oregon'],
	'Canada': ['Nueva York', 'Oregon', 'Terranova', 'Yukon'],
	'chile': ['argentina', 'Australia', 'peru'],
	'China': ['Gobi', 'India', 'Iran', 'Japon', 'Kamchatka', 'Malasia', 'Mongolia', 'Siberia'],
	'colombia': ['brazil', 'Mexico', 'peru'],
	'egypt': ['ethiopia', 'Israel', 'madagascar', 'sahara', 'Polonia', 'Turquia'],
	'Espana': ['Francia', 'Gran Bretana', 'sahara'],
	'ethiopia': ['egypt', 'sahara', 'southafrica', 'zaire'],
	'Francia': ['Alemania', 'Espana', 'Italia'],
	'Gobi': ['China', 'Iran', 'Mongolia'],
	'Gran Bretana': ['Alemania', 'Espana', 'Islandia'],
	'Groenlandia': ['Islandia', 'Labrador', 'Nueva York'],
	'India': ['China', 'Iran', 'Malasia', 'Sumatra'],
	'Iran': ['Aral', 'China', 'Gobi', 'India', 'Mongolia', 'Rusia', 'Turquia'],
	'Islandia': ['Gran Bretana', 'Groenlandia', 'Suecia'],
	'Israel': ['Arabia', 'egypt', 'Turquia'],
	'Italia': ['Alemania', 'Francia'],
	'Japon': ['China', 'Kamchatka'],
	'Java': ['Australia'],
	'Kamchatka': ['Alaska', 'China', 'Japon', 'Siberia'],
	'Labrador': ['Groenlandia', 'Terranova'],
	'madagascar': ['egypt', 'zaire'],
	'Malasia': ['Borneo', 'China', 'India'],
	'Mexico': ['California', 'colombia'],
	'Mongolia': ['Aral', 'China', 'Gobi', 'Iran', 'Siberia'],
	'Nueva York': ['California', 'Canada', 'Groenlandia', 'Oregon', 'Terranova'],
	'Oregon': ['Alaska', 'California', 'Canada', 'Nueva York', 'Yukon'],
	'peru': ['argentina', 'brazil', 'chile', 'colombia'],
	'Polonia': ['Alemania', 'egypt', 'Turquia', 'Rusia'],
	'Rusia': ['Aral', 'Iran', 'Polonia', 'Suecia', 'Turquia'],
	'sahara': ['brazil', 'egypt', 'Espana', 'ethiopia', 'zaire'],
	'Siberia': ['Aral', 'China', 'Kamchatka', 'Mongolia', 'Taimir', 'Tartaria'],
	'southafrica': ['ethiopia', 'zaire'],
	'Suecia': ['Islandia', 'Rusia'],
	'Sumatra': ['Australia', 'India'],
	'Taimir': ['Siberia', 'Tartaria'],
	'Tartaria': ['Aral', 'Siberia', 'Taimir'],
	'Terranova': ['Canada', 'Labrador', 'Nueva York'],
	'Turquia': ['Arabia', 'egypt', 'Iran', 'Israel', 'Polonia', 'Rusia'],
	'uruguay': ['argentina', 'brazil'],
	'Yukon': ['Alaska', 'Canada', 'Oregon'],
	'zaire': ['ethiopia', 'madagascar', 'sahara', 'southafrica'],
}