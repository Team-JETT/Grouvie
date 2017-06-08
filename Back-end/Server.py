import json

from os import environ
from flask import Flask, request
from DBManager import DBManager
from DataParser import DataParser

app = Flask(__name__)

dbManager = None
dParser = None

# Our homepage, should never be directly accessed, unless by devs to make
# sure the web server is actually running.
@app.route("/")
def homepage():
    return "Why are you here?"


# Insert command, used to insert a new entry into the Postgres database.
# Data is received through a POST call to ensure those snooping can't read it.
@app.route("/insert", methods=["GET", "POST"])
def insert():
    entry = json.loads(request.data)
    print entry
    dbManager.insert(entry)
    return "DONE!!!"


# Reads the latitude and longitude provided by the user in order to get local
#  cinema data - includes:
# -- Films being shown.
# -- Closest cinemas
# -- Showtimes of given films at given cinemas
# -- Distance from provided location to cinema in Km.
@app.route("/get_local_data", methods=["GET", "POST"])
def get_local_data():
    phone_data = request.data
    phone_data = json.loads(phone_data)
    return json.dumps(dParser.get_local_data(phone_data['latitude'],
                                             phone_data['longitude'],
                                             phone_data['day'],
                                             phone_data['month'],
                                             phone_data['year']))


if __name__ == "__main__":
    global dbManager, dParser
    dbManager = DBManager()
    dParser = DataParser()
    port = int(environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)
