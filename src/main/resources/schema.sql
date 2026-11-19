CREATE TABLE IF NOT EXISTS books (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, title VARCHAR(200) NOT NULL, author VARCHAR(160) NOT NULL,
 category VARCHAR(100) NOT NULL, total_copies INT NOT NULL CHECK(total_copies >= 0),
 available_copies INT NOT NULL CHECK(available_copies >= 0),
 archived BOOLEAN NOT NULL DEFAULT FALSE, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT chk_book_available_le_total CHECK(available_copies <= total_copies)
);
CREATE TABLE IF NOT EXISTS students (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, full_name VARCHAR(160) NOT NULL, email VARCHAR(190) NOT NULL UNIQUE,
 department VARCHAR(120) NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS book_issues (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, book_id BIGINT NOT NULL, student_id BIGINT NOT NULL,
 issue_date DATE NOT NULL, due_date DATE NOT NULL, return_date DATE NULL,
 status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
 CONSTRAINT fk_issue_book FOREIGN KEY(book_id) REFERENCES books(id),
 CONSTRAINT fk_issue_student FOREIGN KEY(student_id) REFERENCES students(id),
 INDEX idx_issue_status_due(status,due_date), INDEX idx_issue_student(student_id)
);
CREATE TABLE IF NOT EXISTS fines (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, issue_id BIGINT NOT NULL UNIQUE, amount DECIMAL(10,2) NOT NULL CHECK(amount >= 0),
 overdue_days INT NOT NULL DEFAULT 0, paid BOOLEAN NOT NULL DEFAULT FALSE, paid_at TIMESTAMP NULL,
 CONSTRAINT fk_fine_issue FOREIGN KEY(issue_id) REFERENCES book_issues(id)
);
