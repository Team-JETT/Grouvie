from flask import Flask
import requests
import re

MILE_TO_KM = 1.60934
FILM_MATCH_REGEX = r"(?P<film>[^\(]*)"
CINEMA_MATCH_REGEX = r"(?P<cinema>[^,]*)"

FILMS = []
TIMES = []
CINEMAS = []
CINEMA_IDS = []
DISTANCES = []
app = Flask(__name__)


# TODO: PLEASE DON'T USE THESE METHODS TOO MUCH. THEY ACTUALLY QUERY THE API.
# IF YOU'RE GOING TO USE A LOT, SAVE THE DATA YOURSELF FOR TESTING.
class Server:
    """
    This init will start the web server and call
    """
    def __init__(self):
        pass

    """
    Give this function a postcode and CINEMAS, CINEMA_IDS and DISTANCES lists are 
    populated with
    (up to) 5 results.
    """
    def get_cinemas_postcode(self, postcode):
        film_names = requests.get(
            "http://api.cinelist.co.uk/search/cinemas/postcode/{}".format(postcode))
        self.parse_cinema_data(film_names)


    """
    Give this function a longitude and latitude and CINEMAS, CINEMA_IDS and 
    DISTANCES lists are populated with (up to) 5 results.
    """
    def get_cinemas_longlat(self, longitude, latitude):
        film_names = requests.get(
            "https://api.cinelist.co.uk/search/cinemas/coordinates/{}/{}".
            format(longitude, latitude))
        self.parse_cinema_data(film_names)


    def parse_cinema_data(self, film_names):
        cinemas = film_names.json()["cinemas"][:5]
        for i in cinemas:
            # Runs regex over cinemas to remove the location
            CINEMAS.append(re.match(CINEMA_MATCH_REGEX, i['name']).group("cinema"))
            CINEMA_IDS.append(i['id'])
            # Converts distance from mile to km and rounds to 3dp
            DISTANCES.append(round(i['distance'] * MILE_TO_KM, 3))

    """
    Give this function a cinema ID and day and we can populate FILMS with all 
    film showings and times.
    :param day: If no day provided, assume today.
    """
    def get_films(self, cinema_id):
        films = requests.get(
             "http://api.cinelist.co.uk/get/times/cinema/{}".format(cinema_id))
        print films.json()
        for i in films.json()['listings']:
            FILMS.append(i['title'])
            TIMES.append(i['times'])

    @app.route("/")
    def home_page(self):
        return "Hello World!"


if __name__ == "__main__":
    server = Server()
    # server.get_cinemas_postcode("en12lz")
    # server.get_films(10477)
