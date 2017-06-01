import json

from flask import Flask, request, jsonify
from DBManager import DBManager
from DataParser import DataParser

app = Flask(__name__)

dbManager = None
dParser = None


@app.route("/")
def homepage():
    return "Why are you here?"


@app.route("/insert", methods=["GET", "POST"])
def insert():
    print request.data
    # dbManager.update_insert(request.arg())
    return


@app.route("/get_films", methods=["GET", "POST"])
def get_films():
    location = request.data
    location = json.loads(location)
    latitude = location['latitude']
    longitude = location['longitude']
    films = dParser.get_films(latitude, longitude)
    print ','.join(films)
    return ','.join(films)


@app.route("/get_cinemas", methods=["GET", "POST"])
def get_cinemas():
    location = request.get_json()
    cinemas = dParser.get_cinemas_latlong(location['latitude'],
                                          location['longitude'])
    return cinemas


if __name__ == "__main__":
    global dbManager, dParser
    dbManager = DBManager()
    dParser = DataParser()
    app.run(host='0.0.0.0', port=5000)
