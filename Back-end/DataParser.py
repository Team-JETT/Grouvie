from bs4 import BeautifulSoup
from urlparse import urljoin
import requests
import re
import pprint

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

    """
    Give this function a longitude and latitude and CINEMAS, CINEMA_IDS and 
    DISTANCES lists are populated with (up to) 5 results.
    """
    def get_cinemas_latlong(self, latitude, longitude):
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

    """
    Given FILM_NAME, this will find the corresponding movie poster and return
    the wikipedia image url for the movie poster
    """
    def get_film_poster(self, film_name):
        url = 'https://www.google.co.uk/search?q='
        extra = ' film wikipedia'

        # Check for '&' character. We need to replace any of these with 'and'
        # strings as '&' in a url has a different meaning
        if '&' in film_name:
            film_name = 'and'.join(film_name.split('&'))

        wiki_main_url = 'http://en.wikipedia.org/wiki/Main_Page'
        res = requests.get(url + film_name + extra)
        soup = BeautifulSoup(res.text)

        # Parsing the html page to get the first url link in the google search
        # results, which will be the wikipedia page link
        wiki_url = soup.select('.r a')[0].get('href').split('=')[1].split('&')[0]

        # Same as '&' case above
        if '%25' in wiki_url:
            wiki_url = '%'.join(wiki_url.split('%25'))

        res = requests.get(wiki_url)
        soup = BeautifulSoup(res.text)

        # Get the first image tag of the wikipedia page
        img = soup.select('a.image > img')[0]
        img_url = urljoin(wiki_main_url, img['src'])

        return img_url

    """
    Give this function a cinema ID and day and we can populate FILMS with all 
    film showings and times.
    :param day: If no day provided, assume today.
    """
    def get_films_for_cinema(self):
        global CINEMA_CID, CINEMA_DIST
        local_data = {}
        for cinema in CINEMA_CID.keys():
            # Get the cinema ID for a given cinema,
            # E.g. Cineworld London - Enfield: 10477
            cinema_id = CINEMA_CID[cinema]
            # Get list of films showing at this cinema
            films = requests.get(
                "http://api.cinelist.co.uk/get/times/cinema/{}".format(cinema_id))
            # Create a JSON object storing film name, cinema, showtimes and
            # distance to the cinema.
            for i in films.json()['listings']:
                filmname = i['title']
                times = i['times']
                if filmname in local_data:
                    img_url = local_data[filmname][0]['image']
                    local_data[filmname].append(
                        {cinema: [{"showtimes": times,
                                   "distance": CINEMA_DIST[cinema]}],
                         "image": img_url})
                else:
                    img_url = self.get_film_poster(filmname)
                    local_data[filmname] = \
                        [{cinema: [{"showtimes": times,
                                    "distance": CINEMA_DIST[cinema]}],
                          "image": img_url}]
        return local_data

    """Get all film data for your local area."""
    def get_local_data(self, latitude, longitude):
        self.get_cinemas_latlong(latitude, longitude)
        return self.get_films_for_cinema()


if __name__ == '__main__':
    dParser = DataParser()
    pprint.PrettyPrinter(indent=4).pprint(
        dParser.get_local_data(51.636743, -0.069069))



    # """
    # Function that, given the name of the movie FILMNAME, returns FILMNAME, the
    # closest cinema that is showing FILMNAME, the distance from that cinema to
    # the user's postcode in kilometres, and the earliest showtime for FILMNAME
    # in the cinema. It is returned as a tuple in the order mentioned above.
    # """
    # def get_event_from_film(self, filmname):
    #     cid = F_TO_CID[filmname][0]
    #     i = CINEMA_IDS.index(cid)
    #     cine_name = CINEMAS[i]
    #     dist = DISTANCES[i]
    #     showtime = FCID_TO_TIMES[(filmname, cid)][0]
    #     return filmname, cine_name, dist, showtime
    # """

    # *****FOR DEBUGGING PURPOSES*****
    # Function for testing output received from passing FILMNAME into the
    # get_event_from_film method
    # """
    # def print_event_for_film(self, filmname):
    #     print (self.get_event_from_film(self, filmname))
