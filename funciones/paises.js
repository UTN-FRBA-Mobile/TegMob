// Importar para ejecutar funciones: var tools = require('<archivo>');
// ejecute: tools.<funcion>(<variable>);

module.exports = {

    // Obtener los limitrofes, solo los disponibles en la APP
    get_limitrofes: function ( pais ){
        // Chequear si el pais realmente esta dentro de los utilizados.
        if(this.es_utilizado (pais) ){
            var limitrofes = [];
            paises_limitrofes[pais].forEach(element => {
                if (this.es_utilizado(element)){
                    limitrofes.push(element);
                }
            });
            return limitrofes;
        } else {
            return 'array vacio';
        }
    },

    // Paises que declaramos estan dispobles en la APP
    es_utilizado: function ( pais ){
        return(paises_usados_por_nosotros.includes(pais));
	},
	
	// Crea el mapa inicial, recibe el numero de jugadores
	getMapaInicial: function ( n = 6 ){
		var mapa_ini = {}
		var colores = this.shuffleArray( this.getNColoresPosibles(n) )
		var rondas_extra = 2

		paises_usados_por_nosotros.forEach(pais => {
			mapa_ini[pais] = {'owner': colores.pop()}
			if( rondas_extra > 0 ) // paises con mas ejercito para atacar
				mapa_ini[pais].armies = 5
			else
				mapa_ini[pais].armies = 1
			if( colores.length < 1 ){
				colores = this.shuffleArray( this.getNColoresPosibles(n) )
				rondas_extra -= 1
			}
		});
		return mapa_ini;
	},

	shuffleArray: function(array) {
		for (var i = array.length - 1; i > 0; i--) {
			var j = Math.floor(Math.random() * (i + 1));
			var temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
		return array;
	},

	// Colores disponibles
	getNColoresPosibles: function (n = 6){
		return colores.slice(0,n)
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
    'Argentina',
    'Brasil',
    'Chile',
    'Colombia',
	'Uruguay',
	'Peru',
	'Egipto',
	'Etiopia',
	'Madagascar',
	'Sahara',
	'Sudafrica',
	'Zaire'
];

var paises_por_continente = {
	'Africa': ['Egipto', 'Etiopia', 'Madagascar', 'Sahara', 'Sudafrica', 'Zaire'],
	'America del Sur': ['Argentina', 'Brasil', 'Chile', 'Colombia', 'Peru', 'Uruguay'],
	'America del Norte': ['Alaska', 'California', 'Canada', 'Groenlandia', 'Labrador', 'Nueva York', 'Mexico', 'Oregon', 'Terranova', 'Yukon'],
	'Asia': ['Arabia', 'Aral', 'China', 'Gobi', 'India', 'Iran', 'Israel', 'Japon', 'Kamchatka', 'Malasia', 'Mongolia', 'Siberia', 'Taimir', 'Tartaria', 'Turquia'],
	'Europa': ['Alemania', 'Espana', 'Francia', 'Gran Bretana', 'Islandia', 'Italia', 'Polonia', 'Rusia', 'Suecia'],
	'Oceania': ['Australia', 'Borneo', 'Java', 'Sumatra'],
}

var paises_limitrofes = {
	'Alaska': ['Kamchatka', 'Oregon', 'Yukon'],
	'Alemania': ['Francia', 'Gran Bretana', 'Italia', 'Polonia'],
	'Arabia': ['Israel', 'Turquia'],
	'Aral': ['Iran', 'Mongolia', 'Rusia', 'Siberia', 'Tartaria'],
	'Argentina': ['Brasil', 'Chile', 'Peru', 'Uruguay'],
	'Australia': ['Borneo', 'Chile', 'Java', 'Sumatra'],
	'Borneo': ['Australia', 'Malasia'],
	'Brasil': ['Argentina', 'Colombia', 'Peru', 'Sahara', 'Uruguay'],
	'California': ['Mexico', 'Nueva York', 'Oregon'],
	'Canada': ['Nueva York', 'Oregon', 'Terranova', 'Yukon'],
	'Chile': ['Argentina', 'Australia', 'Peru'],
	'China': ['Gobi', 'India', 'Iran', 'Japon', 'Kamchatka', 'Malasia', 'Mongolia', 'Siberia'],
	'Colombia': ['Brasil', 'Mexico', 'Peru'],
	'Egipto': ['Etiopia', 'Israel', 'Madagascar', 'Sahara', 'Polonia', 'Turquia'],
	'Espana': ['Francia', 'Gran Bretana', 'Sahara'],
	'Etiopia': ['Egipto', 'Sahara', 'Sudafrica', 'Zaire'],
	'Francia': ['Alemania', 'Espana', 'Italia'],
	'Gobi': ['China', 'Iran', 'Mongolia'],
	'Gran Bretana': ['Alemania', 'Espana', 'Islandia'],
	'Groenlandia': ['Islandia', 'Labrador', 'Nueva York'],
	'India': ['China', 'Iran', 'Malasia', 'Sumatra'],
	'Iran': ['Aral', 'China', 'Gobi', 'India', 'Mongolia', 'Rusia', 'Turquia'],
	'Islandia': ['Gran Bretana', 'Groenlandia', 'Suecia'],
	'Israel': ['Arabia', 'Egipto', 'Turquia'],
	'Italia': ['Alemania', 'Francia'],
	'Japon': ['China', 'Kamchatka'],
	'Java': ['Australia'],
	'Kamchatka': ['Alaska', 'China', 'Japon', 'Siberia'],
	'Labrador': ['Groenlandia', 'Terranova'],
	'Madagascar': ['Egipto', 'Zaire'],
	'Malasia': ['Borneo', 'China', 'India'],
	'Mexico': ['California', 'Colombia'],
	'Mongolia': ['Aral', 'China', 'Gobi', 'Iran', 'Siberia'],
	'Nueva York': ['California', 'Canada', 'Groenlandia', 'Oregon', 'Terranova'],
	'Oregon': ['Alaska', 'California', 'Canada', 'Nueva York', 'Yukon'],
	'Peru': ['Argentina', 'Brasil', 'Chile', 'Colombia'],
	'Polonia': ['Alemania', 'Egipto', 'Turquia', 'Rusia'],
	'Rusia': ['Aral', 'Iran', 'Polonia', 'Suecia', 'Turquia'],
	'Sahara': ['Brasil', 'Egipto', 'Espana', 'Etiopia', 'Zaire'],
	'Siberia': ['Aral', 'China', 'Kamchatka', 'Mongolia', 'Taimir', 'Tartaria'],
	'Sudafrica': ['Etiopia', 'Zaire'],
	'Suecia': ['Islandia', 'Rusia'],
	'Sumatra': ['Australia', 'India'],
	'Taimir': ['Siberia', 'Tartaria'],
	'Tartaria': ['Aral', 'Siberia', 'Taimir'],
	'Terranova': ['Canada', 'Labrador', 'Nueva York'],
	'Turquia': ['Arabia', 'Egipto', 'Iran', 'Israel', 'Polonia', 'Rusia'],
	'Uruguay': ['Argentina', 'Brasil'],
	'Yukon': ['Alaska', 'Canada', 'Oregon'],
	'Zaire': ['Etiopia', 'Madagascar', 'Sahara', 'Sudafrica'],
}