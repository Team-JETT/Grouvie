from concurrent import futures
from bs4 import BeautifulSoup
import pprint
import concurrent
import requests
import re
import time
import sys

MILE_TO_KM = 1.60934
CINEMA_MATCH_REGEX = r"(?P<cinema>[^,]*)"
NUM_OF_CINEMAS = 25

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
        print latitude, longitude
        sys.stdout.flush()
        global CINEMAS, CINEMA_CID, CINEMA_DIST
        film_names = requests.get(
            "https://api.cinelist.co.uk/search/cinemas/coordinates/{}/{}".
            format(latitude, longitude))
        cinemas = film_names.json()["cinemas"][:NUM_OF_CINEMAS]
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
            "http://api.postcodes.io/postcodes/{}".format(postcode))
        location_data = location_data.json()
        latitude = location_data['result']['latitude']
        longitude = location_data['result']['longitude']
        return round(latitude, 6), round(longitude, 6)

    def get_cinema_url(self, cinema):
        search_url = 'https://www.google.co.uk/search?q=' + cinema
        res = requests.get(search_url)
        soup = BeautifulSoup(res.text, "lxml")

        # Parsing the html page to get the first url link in the google search
        # results, which will be the wikipedia page link
        g_search_res = soup.select('.r a')

        if not g_search_res:
            print 'VERY BAD: No google search results could be obtained'
            sys.stdout.flush()
            return ''

        fst_ref_url = g_search_res[0].get('href')

        if not fst_ref_url:
            print 'RED ALERT: First google search result has no href tag'
            sys.stdout.flush()
            return ''

        cinema_url = fst_ref_url.split('=')[1].split('&')[0]
        print(cinema_url)
        sys.stdout.flush()
        return cinema_url

    def fast_get_film_info(self, film_name):
        """
        Given FILM_NAME, this will find the corresponding movie poster and
        return the image url for the movie poster.
        """
        error_url = 'https://literalminded.files.wordpress.com' \
                    '/2010/11/image-unavailable1.png'
        error_overview = ''
        if '&' in film_name:
            film_name = 'and'.join(film_name.split('&'))

        film_name = film_name.encode('utf-8')
        api_url = 'https://api.themoviedb.org/3/search/movie?api_key=' \
                  'ab499564677631cc1c25f6749d42a16e' \
                  '&language=en-US&query={}'.format(film_name)
        res = requests.get(api_url).json()

        if res['total_results'] == 0:
            return error_url, error_overview

        first_result = res['results'][0]

        poster_path = first_result['poster_path']
        if poster_path is None:
            img_url = error_url
        else:
            img_url = 'http://image.tmdb.org/t/p/w154' + poster_path

        overview = first_result['overview']
        if overview is None:
            overview = error_overview
        else:
            groups = overview.split('.')
            overview = '.'.join(groups[:2])
            if overview[-1] != '.':
                overview += '.'

        return img_url, overview

    def get_films_for_cinemas(self, date):
        """
        Give this function a cinema ID and day and we can populate FILMS with
        all film showings and times.
        :param date: Date to get cinema films for.
        :return:
        """
        global CINEMA_CID, CINEMA_DIST
        local_data = {}

        def get_films_for_cinema(cinema):
            # Get the cinema ID for a given cinema,
            # E.g. Cineworld London - Enfield: 10477
            cinema_id = CINEMA_CID[cinema]
            # Get list of films showing at this cinema
            url = "https://mysterious-eyrie-40497.herokuapp.com/cinemas/{}/" \
                  "showings/{}".format(cinema_id, date)
            #url = "http://moviesapi.herokuapp.com/cinemas/{}/" \
            #      "showings/{}".format(cinema_id, date)
            films = requests.get(url)
            # Create a JSON object storing film name, cinema, showtimes and
            # distance to the cinema.
            films_json = films.json()

            for i in films_json:
                filmname = i["title"]
                times = i['time']
                if filmname in local_data:
                    local_data[filmname]["cinema"].append(
                        {cinema: [{"showtimes": times}]})
                else:
                    local_data[filmname] = {}
                    poster, overview = self.fast_get_film_info(filmname)
                    local_data[filmname]["image"] = poster
                    local_data[filmname]["overview"] = overview
                    local_data[filmname]["cinema"] = \
                        [{cinema: [{"showtimes": times}]}]

        executor = concurrent.futures.ThreadPoolExecutor(NUM_OF_CINEMAS)
        futures = [executor.submit(get_films_for_cinema, cinema_name)
                   for cinema_name in CINEMA_CID.keys()]
        concurrent.futures.wait(futures)

        return local_data

    def parse_date(self, day, month, year):
        """Convert date into a suitable format for use by the external API."""
        day = str(day)
        month = str(month)
        year = str(year)

        if len(day) == 1:
            day = "0" + day
        if len(month) == 1:
            month = "0" + month
        return year + "-" + month + "-" + day

    def get_local_data(self, day, month, year, latitude, longitude):
        """Get all film data for a given location."""
        self.get_cinemas_latlong(latitude, longitude)
        formatted_date = self.parse_date(day, month, year)
        return self.get_films_for_cinemas(formatted_date)


if __name__ == '__main__':
    dParser = DataParser()
    # print dParser.get_latlong("en12lz")
    start_time = time.time()
    pprint.PrettyPrinter(indent=4).pprint(
        dParser.get_local_data(25, 6, 2017, 51.636743, -0.069069))
    print dParser.get_cinema_url('Cineworld fullham road')
    print time.time() - start_time
