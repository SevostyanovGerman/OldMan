package main.constans;


public class RegexpConstans {

	// Регулярное выражение для логина.
	public static final String REG_EXP_OF_LOGIN = "^[A-Za-z]{1,16}$";

	// Регулярное выражение для валидации имени пользователя.
	public static final String REG_EXP_OF_FIRST_NAME = "^[а-яёА-ЯЁa-zA-Z -]{2,50}$";

	// Регулярное выражение для валидации фамилии пользователя.
	public static final String REF_EXP_OF_SECOND_NAME = "^[а-яёА-ЯЁa-zA-Z -]{1,50}$";

	// Регулярное выражение для номера телефона.
	public static final String REG_EXP_OF_PHONE = "^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,20}(\\s*)?$";
}
