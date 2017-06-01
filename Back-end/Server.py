from flask import Flask, jsonify, request
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
    dbManager.update_insert(request.get_json())
    return


@app.route("/get_cinemas", methods=["GET", "POST"])
def get_cinemas():
    location = request.get_json()
    cinemas = dParser.get_cinemas_latlong(location['latitude'],
                                          location['longitude'])
    return cinemas


@app.route("/get_films", methods=["GET", "POST"])
def get_films():
    pass


if __name__ == "__main__":
    global dbManager, dParser
    dbManager = DBManager()
    dParser = DataParser()
    app.run()
