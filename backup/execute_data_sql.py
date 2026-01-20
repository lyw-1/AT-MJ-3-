import mysql.connector
import os

# MySQL连接配置
config = {
    'user': 'root',
    'password': 'Root@2023',
    'host': 'localhost',
    'database': 'mold_digitalization',
    'charset': 'utf8mb4'
}

def execute_sql_file(file_path):
    """执行SQL文件"""
    try:
        # 连接到MySQL
        conn = mysql.connector.connect(**config)
        cursor = conn.cursor()
        
        # 读取SQL文件
        with open(file_path, 'r', encoding='utf-8') as f:
            sql_content = f.read()
        
        # 分割SQL语句（处理多个语句的情况）
        sql_statements = sql_content.split(';')
        
        # 执行每条SQL语句
        for statement in sql_statements:
            statement = statement.strip()
            if statement and not statement.startswith('--'):
                try:
                    cursor.execute(statement)
                    conn.commit()
                    print(f"执行成功: {statement[:50]}...")
                except mysql.connector.Error as e:
                    print(f"执行失败: {statement[:50]}...")
                    print(f"错误信息: {e}")
                    # 继续执行其他语句
        
        # 关闭连接
        cursor.close()
        conn.close()
        print(f"\nSQL文件 {file_path} 执行完成！")
        
    except mysql.connector.Error as e:
        print(f"MySQL连接或执行错误: {e}")

if __name__ == "__main__":
    # 执行data.sql文件
    sql_file = "d:\\trae\\AT-MJ-3\\backend\\backend\\src\\main\\resources\\db\\data.sql"
    execute_sql_file(sql_file)