from sys import stdout
import pprint
import psycopg2

# Make a new Grouvie table to store all the plans
CREATE_GROUVIE = """
CREATE TABLE GROUVIE(
    PHONE_NUMBER        CHAR(11)            NOT NULL,
    LEADER              CHAR(11)            NOT NULL,
    CREATION_DATETIME   CHAR(19)            NOT NULL,
    DATE                CHAR(10),
    SHOWTIME            CHAR(5),
    FILM                TEXT,
    CINEMA              TEXT,
    ACCEPTED            BOOLEAN,
    
    PRIMARY KEY (PHONE_NUMBER, LEADER, CREATION_DATETIME)
)
"""

# Make a new User table to store all user data
CREATE_USERS = """
CREATE TABLE USERS(
    PHONE_NUMBER    CHAR(11)            NOT NULL,
    NAME            TEXT                NOT NULL,
    POSTCODE        TEXT                NOT NULL,
    LATITUDE        NUMERIC(8, 6)       NOT NULL,
    LONGITUDE       NUMERIC(9, 6)       NOT NULL,
    
    PRIMARY KEY (PHONE_NUMBER)
)
"""

# Delete a table
DROP_GROUVIE_TABLE = """
DROP TABLE GROUVIE
"""

# Delete a user table
DROP_USERS_TABLE = """
DROP TABLE USERS
"""

# Insert a new entry into the a table
INSERT_GROUVIE = """
INSERT INTO GROUVIE
VALUES
(%s, %s, %s, %s, %s, %s, %s, %s)
"""

# Insert a new entry into the a table
INSERT_USERS = """
INSERT INTO USERS
VALUES
(%s, %s, %s, %s, %s)
"""

ACCEPT_PLAN = """
UPDATE GROUVIE
SET ACCEPTED = true
WHERE
PHONE_NUMBER = %s AND LEADER = %s AND CREATION_DATETIME = %s
"""

# Update an already existing entry in the Grouvie table
UPDATE_GROUVIE = """
UPDATE GROUVIE
SET DATE = %s, SHOWTIME = %s, FILM = %s, CINEMA = %s
WHERE
PHONE_NUMBER = %s AND LEADER = %s AND CREATION_DATETIME = %s
"""

# Update an already existing entry in the USER table
UPDATE_USERS = """
UPDATE USERS
SET NAME = %s, POSTCODE = %s, LATITUDE = %s, LONGITUDE = %s
WHERE
PHONE_NUMBER = %s
"""

RESET_USER_PREFS = """
UPDATE GROUVIE
SET DATE = NULL, SHOWTIME = NULL, FILM = NULL, CINEMA = NULL, ACCEPTED = FALSE
WHERE LEADER = %s AND CREATION_DATETIME = %s AND (PHONE_NUMBER != LEADER)
"""

# Delete entry from a table given a phone_number, leader and showtime
DELETE_SINGLE = """
DELETE FROM GROUVIE
WHERE PHONE_NUMBER = %s and LEADER = %s and CREATION_DATETIME = %s
"""

# Delete entries from a table given a leader and showtime
DELETE_PLAN = """
DELETE FROM GROUVIE
WHERE LEADER = %s and CREATION_DATETIME = %s
"""

# Get group replies
GROUP_REPLIES = """
SELECT * FROM GROUVIE
WHERE
LEADER = %s AND CREATION_DATETIME = %s
"""

# Display everything in the Grouvie table
SELECT_ALL_GROUVIE = """
SELECT * FROM GROUVIE
"""

# Display everything in the Grouvie table
SELECT_ALL_USERS = """
SELECT * FROM USERS
"""


# Select a single entry from the Grouvie table based on phone_number
SELECT_GROUVIE = """
SELECT * FROM GROUVIE
WHERE
PHONE_NUMBER = %s
"""

# Select a single entry from the Grouvie table based on phone_number
SELECT_USERS = """
SELECT * FROM USERS
WHERE
PHONE_NUMBER = %s
"""

SELECT_VALID_USERS = """
SELECT PHONE_NUMBER, NAME FROM USERS
WHERE
PHONE_NUMBER IN {}
"""

GROUVIE = "GROUVIE"
USER = "USER"


class DBManager:

    # Establish a new connection with the PostgreSQL database.
    # We return the cursor so we can execute on the database, we return the
    # connection so we can close it when we're done.
    def establish_connection(self):
        conn_str = "dbname='g1627137_u' user='g1627137_u'" \
                   "host='db.doc.ic.ac.uk' password='Vk426n3Kjx'"
        try:
            cnxn = psycopg2.connect(conn_str)
            cnxn.autocommit = True
            cursor = cnxn.cursor()
            return cnxn, cursor
        except Exception as e:
            message = e.message + "\nFailed to establish connection. " \
                  "Check connection string."
            exit(message)

    # Close a connection to the database, kills the cursor and the connection.
    def close_connection(self, cnxn, cursor):
        try:
            cursor.close()
            cnxn.close()
        except Exception as e:
            message = e.message + "\nFailed to close connection."
            exit(message)

    # Make a new Grouvie table.
    def make_grouvie_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CREATE_GROUVIE)
        self.close_connection(cnxn, cursor)

    # Make a new Users table.
    def make_user_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CREATE_USERS)
        self.close_connection(cnxn, cursor)

    # Delete a pre-existing table.
    def drop_grouvie_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DROP_GROUVIE_TABLE)
        self.close_connection(cnxn, cursor)

    # Delete a pre-existing table.
    def drop_user_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DROP_USERS_TABLE)
        self.close_connection(cnxn, cursor)

    # Insert a new entry into the Grouvie table.
    def insert_grouvie(self, phone_number, leader, creation_datetime,
                       date, showtime, film, cinema, accepted):
        cnxn, cursor = self.establish_connection()
        cursor.execute(INSERT_GROUVIE, (phone_number, leader, creation_datetime,
                                        date, showtime, film, cinema, accepted))
        self.close_connection(cnxn, cursor)

    def insert_user(self, phone_number, name, postcode, latitude, longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(INSERT_USERS, (phone_number, name, postcode, latitude,
                                      longitude))
        self.close_connection(cnxn, cursor)

    def accept_plan(self, phone_number, leader, creation_datetime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(ACCEPT_PLAN, (phone_number, leader, creation_datetime))
        self.close_connection(cnxn, cursor)

    # Update an entry in the Grouvie table if it exists.
    def update_grouvie(self, phone_number, leader, creation_datetime, date,
                       showtime, film, cinema):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE_GROUVIE, (date, showtime, film, cinema,
                                        phone_number, leader,
                                        creation_datetime))
        self.close_connection(cnxn, cursor)

    # Update an entry in the USERS table if it exists.
    def update_users(self, phone_number, name, postcode, latitude, longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE_USERS, (name, postcode, latitude, longitude,
                                      phone_number))
        self.close_connection(cnxn, cursor)

    # Get group replies for a plan
    def group_replies(self, leader, creation_datetime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(GROUP_REPLIES, (leader, creation_datetime))
        results = cursor.fetchall()
        self.close_connection(cnxn, cursor)

        all_changes = {}
        for i in range(len(results)):
            user = results[i]
            changes_made = {"accepted": user[7]}
            if user[3] is not None:
                changes_made["date"] = user[3]
            if user[4] is not None:
                changes_made["showtime"] = user[4]
            if user[5] is not None:
                changes_made["film"] = user[5]
            if user[6] is not None:
                changes_made["cinema"] = user[6]
            all_changes[user[0]] = changes_made
        print all_changes
        stdout.flush()

        return all_changes

    # Reset all user preferences
    def reset_user_prefs(self, leader, creation_datetime, date,
                         showtime, film,  cinema):
        cnxn, cursor = self.establish_connection()
        cursor.execute(RESET_USER_PREFS, (date, showtime, film, cinema,
                                          leader, creation_datetime))
        self.close_connection(cnxn, cursor)

    # Delete an entry from the table correlating with a user
    def delete_single_grouvie(self, phone_number, leader, creation_datetime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_SINGLE, (phone_number, leader, creation_datetime))
        self.close_connection(cnxn, cursor)

    # Delete entries from the table correlating with a plan
    def delete_plan_grouvie(self, leader, creation_datetime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_PLAN, (leader, creation_datetime))
        self.close_connection(cnxn, cursor)

    # Select an entry in a table based on phone_number.
    def select_grouvie(self, phone_number):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_GROUVIE, phone_number)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result

    # Select an entry in a table based on phone_number.
    def select_users(self, phone_number):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_USERS, tuple(phone_number))
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        # There should only be 1 result so we just return that tuple.
        return result[0] if result else []

    # Select users that actually have a Grouvie account.
    def select_valid_users(self, friends):
        # Build the placeholders which we require when it comes to searching.
        fields = "(" + ','.join(["%s"]*len(friends)) + ")"
        cnxn, cursor = self.establish_connection()
        # friends_tuple = "(" + ",".join(friends) + ")"
        # print friends_tuple
        cursor.execute(SELECT_VALID_USERS.format(fields), tuple(friends))
        print tuple(friends)
        results = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return results

    # Display everything in the Grouvie table.
    def select_all_grouvie(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_ALL_GROUVIE)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result

    # Display everything in the Grouvie table.
    def select_all_users(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_ALL_USERS)
        results = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return results


if __name__ == '__main__':
    data = {'PHONE_NUMBER': "1",
            'LEADER': 0,
            'SHOWTIME': "s",
            'FILM': "GOTG3",
            'CINEMA': "MEMES",
            'ACCEPTED': False}
    query = {'PHONE_NUMBER': "1",
             'LEADER': 0,
             'SHOWTIME': "s"}
    db = DBManager()
    # db.drop_user_table()
    # db.make_user_table()
    # db.insert_user("07587247113", "Erkin", "EN12LZ", 51.636495, -0.069549)
    # db.insert_user("07964006128", "Tarun", "RM65DU", 51.579983, 0.124262)
    # db.insert_user("07942948248", "Jay", "SW100NJ", 51.482079, -0.182265)
    # # print db.select_valid_users(("1", "2", "5", "6"))
    # db.drop_grouvie_table()
    # db.make_grouvie_table()
    pprint.PrettyPrinter(indent=4).pprint(db.select_all_grouvie())
    pprint.PrettyPrinter(indent=4).pprint(db.select_all_users())
    # print db.select_all_users()
    # db.select_valid_users(users)