-------------------------------------------------------
-- Table `Country`
-------------------------------------------------------
CREATE TABLE `Country` (
  `countryId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `countryName` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`countryId`) );
 
-------------------------------------------------------
-- Table `Language`
-------------------------------------------------------
CREATE TABLE `Language` (
  `languageId` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `languageName` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`languageId`) );
 
-------------------------------------------------------
-- Table `Country2Language`
-------------------------------------------------------
CREATE TABLE `Country2Language` (
  `Country_countryId` INT UNSIGNED NOT NULL ,
  `Language_languageId` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`Country_countryId`, `Language_languageId`) ,
  INDEX `fk_Country_has_Language_Language1` (`Language_languageId` ASC) ,
  INDEX `fk_Country_has_Language_Country` (`Country_countryId` ASC) ,
  CONSTRAINT `fk_Country_has_Language_Country`
    FOREIGN KEY (`Country_countryId` )
    REFERENCES `Country` (`countryId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Country_has_Language_Language1`
    FOREIGN KEY (`Language_languageId` )
    REFERENCES `Language` (`languageId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);