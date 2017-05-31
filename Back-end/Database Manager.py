import psycopg2

CREATE_GROUVIE = """
CREATE TABLE GROUVIE(
    PHONE_NUMBER    CHAR(11)           NOT NULL,
    GROUP_ID        SERIAL             NOT NULL,
    SHOWTIME        TIMESTAMP          NOT NULL,
    FILM            TEXT,
    PRICE           NUMERIC(5, 2),
    LOCATION_LAT    NUMERIC(8, 6),
    LOCATION_LONG   NUMERIC(9, 6),
    IMAGE           TEXT,
    
    PRIMARY KEY (PHONE_NUMBER, GROUP_ID, SHOWTIME)
    )
"""

DROP_GROUVIE = """
DROP TABLE GROUVIE
"""


class DBManager:
    cnxn = None
    cursor = None

    def __init__(self):
        pass

    def establish_connection(self):
        global cnxn, cursor
        conn_str = "dbname='g1627137_u' user='g1627137_u' host='db.doc.ic.ac.uk' " \
                   "password='Vk426n3Kjx'"
        cnxn = psycopg2.connect(conn_str)
        cnxn.autocommit = True
        cursor = cnxn.cursor()

    def close_connection(self):
        cursor.close()
        cnxn.close()

    def make_table(self):
        self.establish_connection()
        cursor.execute(CREATE_GROUVIE)
        self.close_connection()

    def drop_table(self):
        self.establish_connection()
        cursor.execute(DROP_GROUVIE)
        self.close_connection()