import csv
import jaydebeapi

# H2 데이터베이스에 연결
connection = jaydebeapi.connect(
    "org.h2.Driver",
    "jdbc:h2:tcp://host.docker.internal:9092/mem:testdb",
    ["sa", ""],
    "/h2.jar"
)

# INSERT 쿼리
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

# 데이터 전처리 함수
def preprocess_row(row):
    # ID, UNIT_CODE, LEVEL, TYPE, ANSWER 추출 및 전처리
    id_value = int(row[0])
    unit_code = int(row[1].replace("uc", ""))  # "uc" 제거 후 정수형 변환
    level = int(row[2])
    type_value = row[3]
    answer = row[4]
    return (id_value, unit_code, level, type_value, answer)

# CSV 파일을 H2 데이터베이스로 가져오는 함수
def import_csv_to_h2(csv_file, table_name, query):
    try:
        # 커서 생성
        with connection.cursor() as cursor:
            # 테이블에 데이터가 이미 있는지 확인
            sql = f"SELECT COUNT(*) FROM {table_name}"
            cursor.execute(sql)
            result = cursor.fetchone()
            if result[0] > 0:
                return

            # 마지막 ID 값을 추적하기 위한 변수
            last_id = 0

            # CSV 파일 열기
            with open(csv_file, 'r', encoding='utf-8') as csvfile:
                csv_reader = csv.reader(csvfile)

                for row in csv_reader:
                    try:
                        # 데이터 전처리
                        processed_data = preprocess_row(row)
                        cursor.execute(query, processed_data)
                        last_id = max(last_id, processed_data[0])  # 마지막 ID 값 업데이트
                    except Exception as e:
                        print(f"데이터 입력 오류: {str(e)}")

            # 변경 사항 커밋
            connection.commit()

            # AUTO_INCREMENT 값을 마지막 ID 값으로 설정
            auto_increment_sql = f"ALTER TABLE {table_name} ALTER COLUMN ID RESTART WITH {last_id + 1}"
            cursor.execute(auto_increment_sql)
            connection.commit()

    finally:
        # 연결 종료
        cursor.close()

# 함수 호출
import_csv_to_h2('problem.csv', 'PROBLEM', insert_problem_table_query)

# 연결 닫기
connection.close()
