import requests
import re
import pprint
import time

MILE_TO_KM = 1.60934
CINEMA_MATCH_REGEX = r"(?P<cinema>[^,]*)"


# TODO: PLEASE DON'T USE THESE METHODS TOO MUCH. THEY ACTUALLY QUERY THE API.
# IF YOU'RE GOING TO USE A LOT, SAVE THE DATA YOURSELF FOR TESTING.
CINEMAS = []
CINEMA_CID = {}
CINEMA_DIST = {}
F_TO_CINEMAS = {}
FCID_TO_TIMES = {}
F_CINEMA_TO_TIMES = {}


class DataParser:
    def get_cinemas_latlong(self, latitude, longitude):
        """
        Give this function a longitude and latitude and CINEMAS, CINEMA_IDS and
        DISTANCES lists are populated with (up to) 5 results.
        """
        global CINEMAS, CINEMA_CID, CINEMA_DIST
        film_names = requests.get(
            "https://api.cinelist.co.uk/search/cinemas/coordinates/{}/{}".
                format(latitude, longitude))
        cinemas = film_names.json()["cinemas"][:5]
        for i in cinemas:
            # Runs regex over cinemas to remove the location
            cinema_name = re.match(CINEMA_MATCH_REGEX, i['name']).group("cinema")
            # Dict storing {cinema name: cinema ID}
            CINEMA_CID[cinema_name] = i['id']
            # Converts distance from mile to km and rounds to 3dp.
            # Dict storing {cinema name: distance}
            CINEMA_DIST[cinema_name] = round(i['distance'] * MILE_TO_KM, 3)

    def get_latlong(self, postcode):
        """
        Give this function a postcode and get the corresponding latitude and
        longitude.
        """
        location_data = requests.get(
            "http://api.postcodes.io/postcodes/{}".format(postcode)
        )
        location_data = location_data.json()
        latitude = location_data['result']['latitude']
        longitude = location_data['result']['longitude']
        return round(latitude, 6), round(longitude, 6)

    def fast_get_film_poster(self, film_name):
        error_url = 'https://literalminded.files.wordpress.com' \
                    '/2010/11/image-unavailable1.png'
        if '&' in film_name:
            film_name = 'and'.join(film_name.split('&'))

        api_url = 'https://api.themoviedb.org/3/search/movie?api_key=' \
                  'ab499564677631cc1c25f6749d42a16e' \
                  '&language=en-US&query={}'.format(film_name)
        res = requests.get(api_url).json()

        if res['total_results'] == 0:
            return error_url

        poster_path = res['results'][0]['poster_path']

        return 'http://image.tmdb.org/t/p/w154' + poster_path

    def get_films_for_cinema(self, date):
        """
        Give this function a cinema ID and day and we can populate FILMS with
        all film showings and times.
        :param date: Date to get cinema films for.
        :return:
        """
        global CINEMA_CID, CINEMA_DIST
        local_data = {}
        for cinema in CINEMA_CID.keys():
            # Get the cinema ID for a given cinema,
            # E.g. Cineworld London - Enfield: 10477
            cinema_id = CINEMA_CID[cinema]
            # Get list of films showing at this cinema
            url = "http://moviesapi.herokuapp.com/cinemas/{}/" \
                  "showings/{}".format(cinema_id, date)
            films = requests.get(url)
            # Create a JSON object storing film name, cinema, showtimes and
            # distance to the cinema.
            for i in films.json():
                filmname = i["title"]
                times = i['time']
                if filmname in local_data:
                    local_data[filmname]["cinema"].append(
                        {cinema: [{"showtimes": times,
                                   "distance": CINEMA_DIST[cinema]}]})
                else:
                    local_data[filmname] = {}
                    local_data[filmname]["image"] = \
                        self.fast_get_film_poster(filmname)
                    local_data[filmname]["cinema"] = \
                        [{cinema: [{"showtimes": times,
                                    "distance": CINEMA_DIST[cinema]}]}]
        return local_data

    def parse_date(self, day, month, year):
        day = str(day)
        month = str(month)
        year = str(year)

        if len(day) == 1:
            day = "0" + day
        if len(month) == 1:
            month = "0" + month
        return year + "-" + month + "-" + day

    def get_local_data(self, latitude, longitude, day, month, year):
        """Get all film data for your local area."""
        self.get_cinemas_latlong(latitude, longitude)
        formatted_date = self.parse_date(day, month, year)
        return self.get_films_for_cinema(formatted_date)


if __name__ == '__main__':
    dParser = DataParser()
    # print dParser.get_latlong("en12lz")
    start_time = time.time()
    pprint.PrettyPrinter(indent=4).pprint(
        dParser.get_local_data(51.636743, -0.069069, 9, 6, 2017))
    print time.time() - start_time
