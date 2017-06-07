import json

from os import environ
from flask import Flask, request
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
    entry = json.loads(request.data)
    print entry
    dbManager.insert(entry)
    return "DONE!!!"


@app.route("/get_local_data", methods=["GET", "POST"])
def get_local_data():
    location = request.data
    location = json.loads(location)
    latitude = location['latitude']
    longitude = location['longitude']
    return json.dumps(dParser.get_local_data(latitude, longitude))


if __name__ == "__main__":
    global dbManager, dParser
    dbManager = DBManager()
    dParser = DataParser()
    port = int(environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)
