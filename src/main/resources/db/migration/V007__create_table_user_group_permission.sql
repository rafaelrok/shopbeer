CREATE TABLE user_employee (
    code BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(120) NOT NULL,
    active BOOLEAN DEFAULT true,
    birth_date DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE group_employee (
    code BIGINT(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permission (
    code BIGINT(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_group (
    code_user_employee BIGINT(20) NOT NULL,
    code_group_employee BIGINT(20) NOT NULL,
    PRIMARY KEY (code_user_employee, code_group_employee),
    FOREIGN KEY (code_user_employee) REFERENCES user_employee(code),
    FOREIGN KEY (code_group_employee) REFERENCES group_employee (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE group_permission (
    code_group_employee BIGINT(20) NOT NULL,
    code_permission BIGINT(20) NOT NULL,
    PRIMARY KEY (code_group_employee, code_permission),
    FOREIGN KEY (code_group_employee) REFERENCES group_employee(code),
    FOREIGN KEY (code_permission) REFERENCES permission(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
