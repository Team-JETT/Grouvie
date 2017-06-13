import json

from os import environ
from flask import Flask, request
from DBManager import DBManager
from DataParser import DataParser

app = Flask(__name__)

dbManager = None
dParser = None

GROUVIE = "GROUVIE"
USER = "USER"

@app.route("/")
def homepage():
    """
    Our homepage, should never be directly accessed, unless by devs to make
    sure the web server is actually running.
    """
    return "Why are you here?"


@app.route("/get_local_data", methods=["GET", "POST"])
def get_local_data():
    """
    Reads the latitude and longitude provided by the user in order to get local
    cinema data - includes:
    -- Films being shown.
    -- Closest cinemas
    -- Showtimes of given films at given cinemas
    -- Distance from provided location to cinema in Km.
    """
    phone_data = json.loads(request.data)
    return json.dumps(dParser.get_local_data(phone_data['latitude'],
                                             phone_data['longitude'],
                                             phone_data['day'],
                                             phone_data['month'],
                                             phone_data['year']))

# TODO: UNTESTED
@app.route("/make_plan", methods=['GET', 'POST'])
def make_plan():
    """Make new plan for all users."""
    phone_data = json.loads(request.data)
    phone_number = phone_data['phone_number']
    leader = phone_data['leader']
    showtime = phone_data['showtime']
    film = phone_data['film']
    cinema = phone_data['cinema']
    latitude = phone_data['latitude']
    longitude = phone_data['longitude']
    # Make a new entry for the group leader.
    dbManager.insert_grouvie(phone_number, leader, showtime, film, cinema,
                             latitude, longitude)
    # Make a new entry in the table for each friend.
    # TODO: What happens if duplicate?
    for friend in phone_data['friends']:
        dbManager.insert_grouvie(friend, leader, showtime, None, None, None,
                                 None)

# TODO: UNTESTED
@app.route("/new_user", methods=['GET', 'POST'])
def new_user():
    """Add postcode date for a given user."""
    phone_data = json.load(request.data)
    latitude, longitude = dParser.get_latlong(phone_data['postcode'])
    dbManager.insert_user(phone_data['phone_number'], phone_data['name'],
                          latitude, longitude)
    return "DONE!!!"

# TODO: UNTESTED
@app.route("/update_postcode", methods=['GET', 'POST'])
def update_postcode():
    """Update postcode data for a given user."""
    phone_data = json.load(request.data)
    latitude, longitude = dParser.get_latlong(phone_data['postcode'])
    dbManager.update_users(phone_data['phone_number'], phone_data['name'],
                           latitude, longitude)
    return "DONE!!!"

# TODO: UNTESTED
app.route("/delete_single", methods=['GET', 'POST'])
def delete_single():
    """Delete an entry from the database - someone can't make it to the plan."""
    phone_data = json.loads(request.data)
    dbManager.delete_single_grouvie(phone_data['phone_number'],
                                    phone_data['leader'],
                                    phone_data['showtime'])
    return "DONE!!!"


# TODO: UNTESTED
@app.route("/delete_plan", methods=['GET', 'POST'])
def delete_plan():
    """Delete a plan from the database, this deletes the plan for all members of
    the plan."""
    phone_data = json.loads(request.data)
    dbManager.delete_plan_grouvie(phone_data['leader'],
                                  phone_data['showtime'])
    return "DONE!!!"


if __name__ == "__main__":
    dbManager = DBManager()
    dParser = DataParser()
    port = int(environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)
