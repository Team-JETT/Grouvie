import psycopg2

# Make a new Grouvie table to store all the plans
CREATE_GROUVIE = """
CREATE TABLE GROUVIE(
    USERNAME        CHAR(16)            NOT NULL,
    LEADER          CHAR(16)            NOT NULL,
    SHOWTIME        CHAR(16)            NOT NULL,
    FILM            TEXT,
    CINEMA          TEXT,
    LATITUDE        NUMERIC(8, 6),
    LONGITUDE       NUMERIC(9, 6),
    
    PRIMARY KEY (USERNAME, LEADER, SHOWTIME)
)
"""

# Make a new User table to store all user data
CREATE_USER = """
CREATE TABLE USER(
    USERNAME        CHAR(30)            NOT NULL,
    POSTCODE        CHAR(7)             NOT NULL,
    
    PRIMARY KEY (USERNAME)
"""

# Delete a table
DROP_TABLE = """
DROP TABLE %s
"""

# Insert a new entry into the a table
INSERT = """
INSERT INTO %s
VALUES %s
"""

# Update an already existing entry in the Grouvie table
UPDATE_GROUVIE = """
UPDATE GROUVIE
SET SHOWTIME = %s, FILM = %s, CINEMA = %s, LATITUDE = %s, 
LONGITUDE = %s
WHERE
USERNAME = %s AND LEADER = %s
"""

# Update an already existing entry in the USER table
UPDATE_USER = """
UPDATE USER
SET POSTCODE = %s
WHERE
USERNAME = %s
"""


# Delete entry from a table given a username, leader and showtime
DELETE_SINGLE = """
DELETE FROM GROUVIE
WHERE USERNAME = %s and LEADER = %s and SHOWTIME = %s
"""
# Delete entries from a table given a leader and showtime
DELETE_PLAN = """
DELETE FROM GROUVIE
WHERE LEADER = %s and SHOWTIME = %s
"""

# Display everything in the Grouvie table
SELECT_ALL = """
SELECT * FROM %s
"""

# Select a single entry from the Grouvie table based on username
SELECT = """
SELECT * FROM %s WHERE
USERNAME = %s
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

    # Make a new User table.
    def make_user_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CREATE_USER)
        self.close_connection(cnxn, cursor)

    # Delete the pre-existing Grouvie table.
    def drop_table(self, table):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DROP_TABLE, table)
        self.close_connection(cnxn, cursor)

    # Insert a new entry into the Grouvie table.
    def insert_grouvie(self, table, username, leader, showtime, film, cinema,
                       latitude, longitude):
        cnxn, cursor = self.establish_connection()
        store_data = str((showtime, film, cinema, latitude, longitude,
                          username, leader))
        cursor.execute(INSERT, (table, store_data))
        self.close_connection(cnxn, cursor)

    def insert_user(self, table, userid, postcode):
        cnxn, cursor = self.establish_connection()
        store_data = str((userid, postcode))
        cursor.execute(INSERT, (table, store_data))
        self.close_connection(cnxn, cursor)

    # Update an entry in the Grouvie table if it exists.
    def update_grouvie(self, username, leader, showtime, film, cinema, latitude,
                       longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE_GROUVIE, (username, leader, showtime, film,
                                        cinema, latitude, longitude))
        self.close_connection(cnxn, cursor)

    # Update an entry in the USER table if it exists.
    def update_user(self, username, postcode):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE_USER, (postcode, username))
        self.close_connection(cnxn, cursor)

    # Delete an entry from the table correlating with a user
    def delete_single_grouvie(self, username, leader, showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_SINGLE, (username, leader, showtime))
        self.close_connection(cnxn, cursor)

    # Delete entries from the table correlating with a plan
    def delete_plan_grouvie(self, leader, showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_PLAN, (leader, showtime))
        self.close_connection(cnxn, cursor)

    # Select an entry in a table based on username.
    def select(self, table, username):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT, (table, username))
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result

    # Display everything in the Grouvie table.
    def selectAll(self, table):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_ALL, table)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result


if __name__ == '__main__':
    data = {'USERNAME': "1",
            'LEADER': 0,
            'SHOWTIME': "s",
            'FILM': "GOTG3",
            'CINEMA': "MEMES",
            'LATITUDE': 52.111100,
            'LONGITUDE': 21.211122}
    query = {'USERNAME': "1",
             'LEADER': 0,
             'SHOWTIME': "s"}
    db = DBManager()
    print db.selectAll("GROUVIE")
