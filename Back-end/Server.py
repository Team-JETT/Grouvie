import json

from os import environ
from flask import Flask, request
from DBManager import DBManager
from DataParser import DataParser

app = Flask(__name__)

dbManager = None
dParser = None


"""
Our homepage, should never be directly accessed, unless by devs to make
sure the web server is actually running.
"""
@app.route("/")
def homepage():
    return "Why are you here?"


"""
Insert command, used to insert a new entry into the Postgres database.
Data is received through a POST call to ensure those snooping can't read it.
"""
@app.route("/insert", methods=["GET", "POST"])
def insert():
    entry = json.loads(request.data)
    print entry
    dbManager.insert(entry)
    return "DONE!!!"


"""
Reads the latitude and longitude provided by the user in order to get local
cinema data - includes:
# -- Films being shown.
# -- Closest cinemas
# -- Showtimes of given films at given cinemas
# -- Distance from provided location to cinema in Km.
"""
@app.route("/get_local_data", methods=["GET", "POST"])
def get_local_data():
    phone_data = json.loads(request.data)
    return json.dumps(dParser.get_local_data(phone_data['latitude'],
                                             phone_data['longitude'],
                                             phone_data['day'],
                                             phone_data['month'],
                                             phone_data['year']))

"""Make new plan for all users."""
# TODO: UNTESTED
@app.route("/make_plan", methods=['GET', 'POST'])
def make_plan():
    phone_data = json.loads(request.data)
    username = phone_data['username']
    leader = phone_data['leader']
    showtime = phone_data['showtime']
    film = phone_data['film']
    cinema = phone_data['cinema']
    latitude = phone_data['latitude']
    longitude = phone_data['longitude']
    # Make a new entry for the group leader.
    dbManager.insert(username, leader, showtime, film, cinema, latitude,
                     longitude)
    # Make a new entry in the table for each friend.
    # TODO: What happens if duplicate?
    for friend in phone_data['friends']:
        dbManager.insert(friend, leader, showtime, None, None, None, None)


"""Check if a user name already exists."""
# TODO: UNTESTED
@app.route("/check_username", methods=['GET', 'POST'])
def check_username():
    result = dbManager.select(request.data)
    print result
    if not result:
        # Return status code '201' for NOT OK
        return '', 201
    else:
        return '', 200


"""Delete an entry from the database - someone can't make it to the plan."""
# TODO: UNTESTED
app.route("/delete_single", methods=['GET', 'POST'])
def delete_single():
    phone_data = json.loads(request.data)
    dbManager.delete_single(phone_data['username'],
                            phone_data['leader'],
                            phone_data['showtime'])
    return "DONE!!!"


"""Delete a plan from the database, this deletes the plan for all members of 
the plan."""
# TODO: UNTESTED
@app.route("/delete_plan", methods=['GET', 'POST'])
def delete_plan():
    phone_data = json.loads(request.data)
    dbManager.delete_plan(phone_data['leader'],
                          phone_data['showtime'])
    return "DONE!!!"


if __name__ == "__main__":
    global dbManager, dParser
    dbManager = DBManager()
    dParser = DataParser()
    port = int(environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)
