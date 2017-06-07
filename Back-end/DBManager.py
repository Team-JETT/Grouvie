import psycopg2

# Make a new Grouvie table to store all the plans
CREATE_GROUVIE = """
CREATE TABLE GROUVIE(
    PHONE_NUMBER    CHAR(11)           NOT NULL,
    GROUP_ID        SERIAL             NOT NULL,
    SHOWTIME        CHAR(16)           NOT NULL,
    FILM            TEXT,
    PRICE           NUMERIC(5, 2),
    LOCATION_LAT    NUMERIC(8, 6),
    LOCATION_LONG   NUMERIC(9, 6),
    IMAGE           TEXT,
    IS_LEADER       BIT,
    
    PRIMARY KEY (PHONE_NUMBER, GROUP_ID, SHOWTIME)
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
(%s, %s, %s, %s, %s, %s, %s, %s, %s)
"""

# Update an already existing entry in the Grouvie table
UPDATE = """
UPDATE GROUVIE
SET SHOWTIME = %s, FILM = %s, PRICE = %s, LOCATION_LAT = %s, 
LOCATION_LONG = %s, IMAGE = %s, IS_LEADER = %s
WHERE
PHONE_NUMBER = %s AND GROUP_ID = %s
"""

# Display everything in the Grouvie table
SELECT_ALL = """
SELECT * FROM GROUVIE
"""

# Select a single entry from the Grouvie table based on phone number and
# group id
SELECT = """
SELECT * FROM GROUVIE WHERE
PHONE_NUMBER = %s AND GROUP_ID = %s
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
    def insert(self, data):
        cnxn, cursor = self.establish_connection()
        cursor.execute(INSERT, (
            data['PHONE_NUMBER'],
            data['GROUP_ID'],
            data['SHOWTIME'],
            data['FILM'],
            data['PRICE'],
            data['LOCATION_LAT'],
            data['LOCATION_LONG'],
            data['IMAGE'],
            data['IS_LEADER']
        ))
        self.close_connection(cnxn, cursor)

    # Update an entry in the Grouvie table if it exists, otherwise, make a
    # new entry.
    def update_insert(self, data):
        cnxn, cursor = self.establish_connection()
        cursor.execute(UPDATE, (
            data['SHOWTIME'],
            data['FILM'],
            data['PRICE'],
            data['LOCATION_LAT'],
            data['LOCATION_LONG'],
            data['IMAGE'],
            data['IS_LEADER'],
            data['PHONE_NUMBER'],
            data['GROUP_ID']
        ))
        self.insert(data)
        self.close_connection(cnxn, cursor)

    # Select an entry in the Grouvie table based on phone number and group id.
    def select(self, query):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT, (query['PHONE_NUMBER'], query['GROUP_ID']))
        print cursor.fetchall()
        self.close_connection(cnxn, cursor)

    # Display everything in the Grouvie table.
    def selectAll(self):
        cnxn, cursor = self.establish_connection()
        cursor.execute(SELECT_ALL)
        result = cursor.fetchall()
        self.close_connection(cnxn, cursor)
        return result


if __name__ == '__main__':
    data = {'PHONE_NUMBER': "1",
            'GROUP_ID': 0,
            'SHOWTIME': "s",
            'FILM': "GOTG3",
            'PRICE': 32.22,
            'LOCATION_LAT': 52.111100,
            'LOCATION_LONG': 21.211122,
            'IMAGE': "HTTP",
            'IS_LEADER': '0'}
    query = {'PHONE_NUMBER': "1",
             'GROUP_ID': 0,
             'SHOWTIME': "s"}
    db = DBManager()
    print db.selectAll()
