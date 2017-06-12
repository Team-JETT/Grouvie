import psycopg2

# Make a new Grouvie table to store all the plans
CREATE_GROUVIE = """
CREATE TABLE GROUVIE(
    USERNAME        CHAR(16)           NOT NULL,
    LEADER          CHAR(16)           NOT NULL,
    SHOWTIME        CHAR(16)           NOT NULL,
    FILM            TEXT,
    CINEMA          TEXT,
    LATITUDE        NUMERIC(8, 6),
    LONGITUDE       NUMERIC(9, 6),
    
    PRIMARY KEY (USERNAME, LEADER, SHOWTIME)
)
"""

# Delete our Grouvie table
DROP_GROUVIE = """
DROP TABLE GROUVIE
"""

# Insert a new entry into the Grouvie table
INSERT = """
INSERT INTO GROUVIE
VALUES
(%s, %s, %s, %s, %s, %s, %s)
"""

# Update an already existing entry in the Grouvie table
UPDATE = """
UPDATE GROUVIE
SET SHOWTIME = %s, FILM = %s, CINEMA = %s, LATITUDE = %s, 
LONGITUDE = %s
WHERE
USERNAME = %s AND LEADER = %s
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
SELECT * FROM GROUVIE
"""

# Select a single entry from the Grouvie table based on username
SELECT = """
SELECT * FROM GROUVIE WHERE
USERNAME = %s
"""


class DBManager:

    # Establish a new connection with the PosgreSQL database.
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
    def make_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(CREATE_GROUVIE)
        self.close_connection(cnxn, cursor)

    # Delete the pre-existing Grouvie table.
    def drop_table(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DROP_GROUVIE)
        self.close_connection(cnxn, cursor)

    # Insert a new entry into the Grouvie table.
    def insert(self, username, leader, showtime, film, cinema, latitude,
               longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(INSERT, (username, leader, showtime, film, cinema,
                                latitude, longitude))
        self.close_connection(cnxn, cursor)

    # Update an entry in the Grouvie table if it exists, otherwise, make a
    # new entry.
    def update(self, username, leader, showtime, film, cinema, latitude,
               longitude):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE, (username, leader, showtime, film, cinema,
                                latitude, longitude))
        self.close_connection(cnxn, cursor)

    # Delete an entry from the table correlating with a user
    def delete_single(self, username, leader, showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_SINGLE, username, leader, showtime)
        self.close_connection(cnxn, cursor)

    # Delete entries from the table correlating with a plan
    def delete_plan(self, leader, showtime):
        cnxn, cursor = self.establish_connection()
        cursor.execute(DELETE_PLAN, leader, showtime)
        self.close_connection(cnxn, cursor)

    # Select an entry in the Grouvie table based on username.
    def select(self, username):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT, username)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result

    # Display everything in the Grouvie table.
    def selectAll(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_ALL)
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
    db.drop_table()
    db.make_table()
