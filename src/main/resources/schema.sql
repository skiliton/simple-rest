DROP TABLE IF EXISTS cars;

CREATE TABLE cars (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      brand VARCHAR(250) NOT NULL,
                      model VARCHAR(250) NOT NULL,
                      seats INT NOT NULL,
                      description VARCHAR(250) NOT NULL
);
