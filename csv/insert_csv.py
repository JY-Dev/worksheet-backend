import csv
import jaydebeapi

connection = jaydebeapi.connect(
    "org.h2.Driver",
    "jdbc:h2:tcp://host.docker.internal:9092/mem:testdb",
    ["sa", ""],
    "/h2.jar"
)

insert_problem_table_query = """
    INSERT INTO PROBLEM (
        ID,
        UNIT_CODE,
        DIFFICULTY,
        TYPE,
        ANSWER,
        CT_UTC,
        UT_UTC
    ) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
"""

insert_student_table_query = """
    INSERT INTO STUDENT (
        ID,
        NAME,
        CT_UTC,
        UT_UTC
    ) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
"""

insert_teacher_table_query = """
    INSERT INTO TEACHER (
        ID,
        NAME,
        CT_UTC,
        UT_UTC
    ) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
"""

def preprocess_problem_row(row):
    id_value = int(row[0])
    unit_code = int(row[1].replace("uc", ""))
    difficulty = int(row[2])
    type_value = row[3]
    answer = row[4]
    return (id_value, unit_code, difficulty, type_value, answer)

def import_problem_csv_to_h2(csv_file):
    try:
        with connection.cursor() as cursor:
            cursor.execute("SELECT COUNT(*) FROM PROBLEM")
            result = cursor.fetchone()
            if result[0] > 0:
                return

            last_id = 0

            with open(csv_file, 'r', encoding='utf-8') as csvfile:
                csv_reader = csv.reader(csvfile)

                for row in csv_reader:
                    try:
                        processed_data = preprocess_problem_row(row)
                        cursor.execute(insert_problem_table_query, processed_data)
                        last_id = max(last_id, processed_data[0])
                    except Exception as e:
                        print(f"데이터 입력 오류: {str(e)}")
            connection.commit()

            cursor.execute(f"ALTER TABLE PROBLEM ALTER COLUMN ID RESTART WITH {last_id + 1}")
            connection.commit()

    finally:
        # 연결 종료
        cursor.close()

def import_simple_csv_to_h2(csv_file, table_name, query):
    try:
        with connection.cursor() as cursor:
            cursor.execute(f"SELECT COUNT(*) FROM {table_name}")
            result = cursor.fetchone()
            if result[0] > 0:
                return

            # CSV 파일 열기
            with open(csv_file, 'r', encoding='utf-8') as csvfile:
                csv_reader = csv.reader(csvfile)

                for row in csv_reader:
                    try:
                        cursor.execute(query, row)
                    except Exception as e:
                        print(f"데이터 입력 오류: {str(e)}")

            connection.commit()

    finally:
        cursor.close()

import_problem_csv_to_h2('problem.csv')
import_simple_csv_to_h2('student.csv', 'STUDENT', insert_student_table_query)
import_simple_csv_to_h2('teacher.csv', 'TEACHER', insert_teacher_table_query)

connection.close()