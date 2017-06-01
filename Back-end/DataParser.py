#!/usr/bin/python3

import requests
import re

MILE_TO_KM = 1.60934
FILM_MATCH_REGEX = r"(?P<film>[^\(]*)"
CINEMA_MATCH_REGEX = r"(?P<cinema>[^,]*)"



# TODO: PLEASE DON'T USE THESE METHODS TOO MUCH. THEY ACTUALLY QUERY THE API.
# IF YOU'RE GOING TO USE A LOT, SAVE THE DATA YOURSELF FOR TESTING.
FILMS = []
TIMES = []
CINEMAS = []
CINEMA_IDS = []
DISTANCES = []
F_TO_CID = {}
FCID_TO_TIMES = {}
class DataParser:

    """
    Give this function a longitude and latitude and CINEMAS, CINEMA_IDS and 
    DISTANCES lists are populated with (up to) 5 results.
    """
    def get_cinemas_latlong(self, latitude, longitude):
        global CINEMA_IDS, DISTANCES, CINEMAS
        film_names = requests.get(
            "https://api.cinelist.co.uk/search/cinemas/coordinates/{}/{}".
                format(latitude, longitude))
        cinemas = film_names.json()["cinemas"][:5]
        for i in cinemas:
            # Runs regex over cinemas to remove the location
            cinema_name = re.match(CINEMA_MATCH_REGEX, i['name']).group("cinema")
            CINEMAS.append(cinema_name)
            # Bind the Cinema ID's to their cinema names
            CINEMA_IDS.append(i['id'])
            # Converts distance from mile to km and rounds to 3dp
            DISTANCES.append(round(i['distance'] * MILE_TO_KM, 3))

    """
    Give this function a cinema ID and day and we can populate FILMS with all 
    film showings and times.
    :param day: If no day provided, assume today.
    """
    def get_films_for_cinema_id(self, cinema_id):
        global FILMS, TIMES, F_TO_CID, FCID_TO_TIMES
        films = requests.get(
            "http://api.cinelist.co.uk/get/times/cinema/{}".format(cinema_id))
        for i in films.json()['listings']:
            filmname = i['title']
            times = i['times']
            FILMS.append(filmname)
            TIMES.append(times)
            if filmname in F_TO_CID:
                F_TO_CID[filmname].append(cinema_id)
            else:
                F_TO_CID[filmname] = [cinema_id]
            FCID_TO_TIMES[(filmname, cinema_id)] = times

    """Get all films showing in your local cinemas."""
    def get_films(self, latitude, longitude):
        self.get_cinemas_latlong(latitude, longitude)
        for i in CINEMA_IDS:
            self.get_films_for_cinema_id(i)
        return list(set(FILMS))


    """Get all cinemas in your area showing a given film."""
    def get_cinemas(self, filmname):
        global F_TO_CID, CINEMA_IDS, CINEMAS
        cids = F_TO_CID[filmname]
        cinemas_showing_film = []
        for cid in cids:
            cid_index = CINEMA_IDS.index(cid)
            cinemas_showing_film.append(CINEMAS[cid_index])
        return cinemas_showing_film



if __name__ == '__main__':
    dParser = DataParser()
    print dParser.get_films(51.636743, -0.069069)
    print dParser.get_cinemas("The Boss Baby")



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
