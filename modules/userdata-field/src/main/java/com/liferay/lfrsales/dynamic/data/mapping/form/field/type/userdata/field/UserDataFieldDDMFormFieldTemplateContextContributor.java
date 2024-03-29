package com.liferay.lfrsales.dynamic.data.mapping.form.field.type.userdata.field;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Address;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlParserUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true, property = "ddm.form.field.type.name=userdataField",
		service = {
			DDMFormFieldTemplateContextContributor.class,
			UserDataFieldDDMFormFieldTemplateContextContributor.class
		}
	)
public class UserDataFieldDDMFormFieldTemplateContextContributor implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put(
			"userDataField", (String)ddmFormField.getProperty("userDataField"));

		String predefinedValue = null;

		try {
			predefinedValue = getPredefinedValue(
				ddmFormField, ddmFormFieldRenderingContext);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		parameters.put("predefinedValue", predefinedValue);

		String value = getValue(ddmFormFieldRenderingContext);

		if (Validator.isNotNull(value)) {
			parameters.put("value", value);
		}
		else {
			parameters.put("value", predefinedValue);
		}

		return parameters;
	}

	protected String getPredefinedValue(
			DDMFormField ddmFormField,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws Exception {

		HttpServletRequest httpServletRequest =
			ddmFormFieldRenderingContext.getHttpServletRequest();
		
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String predefinedValue = null;

		if (themeDisplay.isSignedIn()) {
			String userDataField = (String)ddmFormField.getProperty(
				"userDataField");

			if (_log.isDebugEnabled()) {
				_log.debug("Data Selected:" + userDataField);
			}

			String methodName = userDataField.replace("[", "");

			methodName = methodName.replace("]", "");
			methodName = methodName.replace("\"", "");

			User user = themeDisplay.getUser();
			
			if (_log.isDebugEnabled()) {
				_log.debug("User Detected: " + user.getEmailAddress());
			}


			List<Address> addresses = user.getAddresses();

			Address address = null;

			if (!addresses.isEmpty()) {
				address = addresses.get(0);
			}

			if (methodName.equals("getDateOfBirth")) {
				Date birthday = null;

				try {
					birthday = user.getBirthday();

					if (birthday != null) {
						DateFormat dateFormat = new SimpleDateFormat(
							"MM/dd/yyyy");

						predefinedValue = dateFormat.format(birthday);
					}
				}
				catch (PortalException portalException) {
					_log.error(portalException, portalException);
				}
			}
			else if (methodName.equals("getPhones")) {
				List<Phone> userPhones = user.getPhones();

				predefinedValue = "";

				if (!userPhones.isEmpty()) {
					Phone firstUserPhone = userPhones.get(0);

					predefinedValue = firstUserPhone.getNumber();
				}
			}
			else if (methodName.equals("getAddresses")) {
				predefinedValue = "";

				if (address != null) {
					Country addressCountry = address.getCountry();

					predefinedValue =
						address.getStreet1() + ", " + address.getCity() + ", " +
							address.getZip() + ", " + addressCountry.getName();
				}
			}
			else if (methodName.equals("getStreet1")) {
				if (address != null) {
					predefinedValue = address.getStreet1();
				}
			}
			else if (methodName.equals("getStreet2")) {
				if (address != null) {
					predefinedValue = address.getStreet2();
				}
			}
			else if (methodName.equals("getStreet3")) {
				if (address != null) {
					predefinedValue = address.getStreet3();
				}
			}
			else if (methodName.equals("getCity")) {
				if (address != null) {
					predefinedValue = address.getCity();
				}
			}
			else if (methodName.equals("getCountryId")) {
				if (address != null) {
					predefinedValue = String.valueOf(address.getCountryId());
				}
			}
			else if (methodName.equals("getCountryName")) {
				if (address != null) {
					Country addressCountry = address.getCountry();

					predefinedValue = addressCountry.getName();
				}
			}
			else if (methodName.equals("getRegionCode")) {
				if (address != null) {
					Region addressRegion = address.getRegion();

					predefinedValue = addressRegion.getRegionCode();
				}
			}
			else if (methodName.equals("getRegionId")) {
				if (address != null) {
					predefinedValue = String.valueOf(address.getRegionId());
				}
			}
			else if (methodName.equals("getPostalCode")) {
				if (address != null) {
					predefinedValue = address.getZip();
				}
			}
			else {
				Method m = User.class.getMethod(methodName);

				predefinedValue = (String)m.invoke(user);
			}
		}

		return predefinedValue;
	}

	protected String getValue(
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		String value = String.valueOf(
			ddmFormFieldRenderingContext.getProperty("value"));

		if (ddmFormFieldRenderingContext.isViewMode()) {
			value = HtmlParserUtil.extractText(value);
		}

		return value;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserDataFieldDDMFormFieldTemplateContextContributor.class);

}
