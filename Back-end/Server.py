from sys import stdout
from decimal import *
import simplejson as json

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
    getcontext().prec = 6
    for friend in friends:
        result = dbManager.select_users([friend])
        # Safety net in case the friend doesn't actually have a Grouvie account
        if result:
            total_latitude += result[3]
            total_longitude += result[4]
    return total_latitude, total_longitude


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
    friends = phone_data['friends']
    leader_latitude, leader_longitude = \
        dParser.get_latlong(phone_data['postcode'])

    friends = friends[1:len(friends) - 1].split(", ")
    total_latitude, total_longitude = \
        calculate_avg_location(friends)
    total_members = len(friends) + 1
    avg_latitude = (total_latitude + Decimal(leader_latitude)) / total_members
    avg_longitude = (total_longitude + Decimal(leader_longitude)) / \
                    total_members
    print "TOTAL MEMBERS: " + str(total_members)
    stdout.flush()
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
    creation_datetime = phone_data['creation_datetime']
    date = phone_data['date']
    showtime = phone_data['showtime']
    film = phone_data['film']
    cinema = phone_data['cinema']
    print "Make plan [" + phone_number + ", " + leader + ", " \
          + creation_datetime + ", " + date + ", " + showtime +\
          ", " + film + ", " + cinema + "]"
    # Make a new entry for the group leader.
    dbManager.insert_grouvie(phone_number, leader, creation_datetime,
                             date, showtime, film, cinema, False)
    # Make a new entry in the table for each friend.
    # TODO: What happens if duplicate?
    friends = phone_data['friends']
    friends = friends[1:len(friends) - 1].split(", ")
    print str(friends)
    for friend in friends:
        print str(friend)
        dbManager.insert_grouvie(friend, leader, creation_datetime, None,
                                 None, None, None, False)
    print "MADE NEW PLAN"
    stdout.flush()
    return ''


# TODO: UNTESTED
@app.route("/confirm_plan", methods=['GET', 'POST'])
def confirm_plan():
    phone_data = json.loads(request.data)
    dbManager.confirm_plan(phone_data['leader'],
                           phone_data['creation_datetime'])
    return ''


# TODO: UNTESTED
@app.route("/is_plan_confirmed", methods=['GET', 'POST'])
def is_plan_confirmed():
    phone_data = json.loads(request.data)
    result = dbManager.is_plan_confirmed(phone_data['leader'],
                                         phone_data['creation_datetime'])
    print str(result)
    stdout.flush()
    return str(result)


@app.route("/new_user", methods=['GET', 'POST'])
def new_user():
    """Add postcode date for a given user."""
    phone_data = json.loads(request.data)
    latitude, longitude = dParser.get_latlong(phone_data['postcode'])
    dbManager.insert_user(phone_data['phone_number'], phone_data['name'],
                          phone_data['postcode'], latitude, longitude)
    print "ADDED NEW USER."
    stdout.flush()
    return "DONE!!!"


@app.route("/verify_user", methods=['GET', 'POST'])
def verify_user():
    """Given a phone number, verifies that the user is a Grouvie user."""
    user = request.data
    print user
    # Convert user to tuple before passing to select_valid_users
    results = dbManager.select_valid_users([user])
    # If the user is in the database, give return code 1, otherwise, 0
    print "VALID USER" if results else "INVALID USER"
    stdout.flush()
    return "1" if results else "0"


@app.route("/verify_friends", methods=['GET', 'POST'])
def verify_friends():
    """Given a list of phone numbers we verify if the owner of that contact
    number is a user of Grouvie."""
    # Convert received data to tuple suitable for placing in DB query.
    friends = request.data
    friends = friends[1:len(friends)-1].split(", ")
    valid_users = dbManager.select_valid_users(friends)
    valid_friends = {}
    for user in valid_users:
        valid_friends[user[0]] = user[1]
    stdout.flush()
    print "VALID FRIENDS:", valid_friends
    return json.dumps(valid_friends)


@app.route("/get_user", methods=['GET', 'POST'])
def get_user():
    """Given a user phone number, gets the users personal data."""
    phone_number = request.data
    print phone_number
    user_data = dbManager.select_users([phone_number])
    json_data = {"phone_number": user_data[0],
                 "name": user_data[1],
                 "postcode": user_data[2]}
    print "USER: " + json.dumps(json_data, use_decimal=True)
    stdout.flush()
    return json.dumps(json_data, use_decimal=True)


# TODO: UNTESTED
@app.route("/update_postcode", methods=['GET', 'POST'])
def update_postcode():
    """Update postcode data for a given user."""
    phone_data = json.loads(request.data)
    latitude, longitude = dParser.get_latlong(phone_data['postcode'])
    dbManager.update_users(phone_data['phone_number'], phone_data['name'],
                           phone_data['postcode'], latitude, longitude)
    print "DONE!!!"
    stdout.flush()
    return


# TODO: UNTESTED
@app.route("/suggest_plan", methods=['GET', 'POST'])
def suggest_plan():
    phone_data = json.loads(request.data)
    dbManager.update_grouvie(phone_data['phone_number'],
                             phone_data['leader'],
                             phone_data['creation_datetime'],
                             phone_data['date'],
                             phone_data['showtime'],
                             phone_data['film'],
                             phone_data['cinema'])


# TODO: UNTESTED
@app.route("/accept_plan", methods=['GET', 'POST'])
def accept_plan():
    phone_data = json.loads(request.data)
    print "ACCEPT PLAN: " + str(phone_data)
    stdout.flush()
    dbManager.accept_plan(phone_data['phone_number'],
                          phone_data['leader'],
                          phone_data['creation_datetime'])
    return ''


# TODO: UNTESTED
@app.route("/group_replies", methods=['GET', 'POST'])
def group_replies():
    phone_data = json.loads(request.data)
    print "GROUP REPLIES " + str(phone_data)
    stdout.flush()
    return json.dumps(dbManager.group_replies(phone_data['leader'],
                                              phone_data['creation_datetime']))


# TODO: UNTESTED
@app.route("/reset_user_prefs", methods=['GET', 'POST'])
def reset_user_prefs():
    phone_data = json.loads(request.data)
    print "RESET USER PREFS: " + str(phone_data)
    stdout.flush()
    dbManager.reset_user_prefs(phone_data['leader'],
                               phone_data['creation_datetime'],
                               phone_data['date'],
                               phone_data['showtime'],
                               phone_data['film'],
                               phone_data['cinema'])

# TODO: UNTESTED
@app.route("/delete_single", methods=['GET', 'POST'])
def delete_single():
    """Delete an entry from the database - someone can't make it to the plan."""
    phone_data = json.loads(request.data)
    print phone_data
    stdout.flush()
    dbManager.delete_single_grouvie(phone_data['phone_number'],
                                    phone_data['leader'],
                                    phone_data['creation_datetime'])
    print "SOMEONE CANT GO"
    return ''


# TODO: UNTESTED
@app.route("/delete_plan", methods=['GET', 'POST'])
def delete_plan():
    """Delete a plan from the database, this deletes the plan for all members
    of the plan."""
    phone_data = json.loads(request.data)
    dbManager.delete_plan_grouvie(phone_data['leader'],
                                  phone_data['creation_datetime'])
    stdout.flush()
    print "DELETED PLAN"
    stdout.flush()
    return ''

@app.route("/get_cinema_url", methods=['GET', 'POST'])
def get_cinema_url():
    cinema_name = request.data
    return dParser.get_cinema_url(cinema_name)

if __name__ == "__main__":
    dbManager = DBManager()
    dParser = DataParser()
    port = int(environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port)
