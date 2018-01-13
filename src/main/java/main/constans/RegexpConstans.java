package main.constans;

public class RegexpConstans {

	// Регулярное выражение для логина.
	public static final String REG_EXP_OF_LOGIN = "^[A-Za-z]{1,16}$";

	// Регулярное выражение для валидации имени пользователя.
	public static final String REG_EXP_OF_FIRST_NAME = "^[а-яёА-ЯЁa-zA-Z -]{2,50}$";

	// Регулярное выражение для валидации фамилии пользователя.
	public static final String REF_EXP_OF_SECOND_NAME = "^[- а-яёА-ЯЁa-zA-Z]{1,50}$";

	// Регулярное выражение для номера телефона.
	public static final String REG_EXP_OF_PHONE = "^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,20}(\\s*)?$";

	// Регулярное выражение для общих случаев
	public static final String REG_EXP_GENERAL = "^[- а-яёА-ЯЁa-zA-Z0-9]{2,50}$";

	// Выражение задающее формат цвета в 16-ричной системе счисления
	public static final String REG_EXP_COLOR = "^[#]{1}[0-9A-Fa-f]{6}$";

	// Регулярное выражение для валидации модели телефона.
	public static final String REG_EXP_OF_MODEL_PHONE = "^[а-яёА-ЯЁa-zA-Z0-9 -]{2,50}$";

	// Регулярное выражение для домашней страницы должности.
	public static final String REG_EXP_OF_ROLE_URL = "^[- /а-яёА-ЯЁa-zA-Z0-9]{2,20}$";

	// Регулярное выражение для валидации номера заказа.
	public static final String REG_EXP_ORDER_NUMBER = "^[\\d]{1,15}$";

	// Регулярное выражение для валидации индекса почтового отделения.
	public static final String REG_EXP_ZIP = "^[\\d]{3,6}$";
}
