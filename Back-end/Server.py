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


def calculate_avg_location(friends):
    total_latitude, total_longitude = 0, 0
    for friend in friends:
        # There should be only 1 result
        result = dbManager.select_users(friend)[0]
        total_latitude += result[3]
        total_longitude += result[4]
    members = len(friends)
    avg_latitude = total_longitude / members
    avg_longitude = total_longitude / members
    return avg_latitude, avg_longitude


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
    avg_latitude, avg_longitude = calculate_avg_location(phone_data['friends'])
    return json.dumps(dParser.get_local_data(phone_data['day'],
                                             phone_data['month'],
                                             phone_data['year'],
                                             avg_latitude, avg_longitude))


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

@app.route("/verify_user", methods=['GET', 'POST'])
def verify_user():
    """Given a phone number, verifies that the user is a Grouvie user."""
    user = request.data
    # Convert user to tuple before passing to select_valid_users
    results = dbManager.select_valid_users(user,)
    # If the user is in the database, give status code 200, otherwise, 201.
    return '', 200 if results else '', 201

@app.route("/verify_friends", methods=['GET', 'POST'])
def verify_friends():
    """Given a list of phone numbers we verify if the owner of that contact
    number is a user of Grouvie."""
    # Convert received data to tuple suitable for placing in DB query.
    friends = tuple(request.data)
    valid_users = dbManager.select_valid_users(friends)
    valid_friends = {}
    for user in valid_users:
        valid_friends[user[0]] = user[1]
    return json.dumps(valid_friends)


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
    """Delete a plan from the database, this deletes the plan for all members
    of the plan."""
    phone_data = json.loads(request.data)
    dbManager.delete_plan_grouvie(phone_data['leader'],
                                  phone_data['showtime'])
    return "DONE!!!"


if __name__ == "__main__":
    dbManager = DBManager()
    dParser = DataParser()
    port = int(environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)
