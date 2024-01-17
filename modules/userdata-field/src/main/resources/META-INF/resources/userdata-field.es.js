import React, {useEffect} from 'react';
import {usePrevious} from '@liferay/frontend-js-react-web';
import {
	EVENT_TYPES as CORE_EVENT_TYPES,
	PagesVisitor,
	useForm,
	useFormState,
} from 'data-engine-js-components-web';
import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';
import {useSyncValue} from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';
import {ClayInput} from '@clayui/form';
const UserDataFieldDDMFormFieldType = ({name, onChange, predefinedValue, readOnly, value}) => (
	<ClayInput
		className="ddm-field-text"
		disabled={readOnly}
		name={name}
		onInput={(event) => {
			onChange(event);
		}}
		type="text"
		value={value ? value : predefinedValue}
	/>
);

const Main = props => {

	const {
		label,
		name,
		onChange,
		predefinedValue = '',
		readOnly,
		value,
		...otherProps
	} = props;

	const [currentValue, setCurrentValue] = useSyncValue(
		value ? value : predefinedValue
	);
	const dispatch = useForm();
	const {editingLanguageId, pages} = useFormState();
	const previousValue = usePrevious(value);

	useEffect(() => {
		// If the value has changed
		if (value && previousValue !== value) {
			const pagesVisitor = new PagesVisitor(pages);

			dispatch({
				payload: pagesVisitor.mapFields(
					(field) => {
						console.log("test: "+field.fieldReference);
						if (field.fieldReference === 'email') {
							const anotherFieldValue = value;

							return {
								...field,
								localizedValue: {
									...field.localizedValue,
									[editingLanguageId]: anotherFieldValue,
								},
								value: anotherFieldValue,
							};

						}

						return field;
					},
					false,
					true
				),
				type: CORE_EVENT_TYPES.PAGE.UPDATE,
			});
		}
		if (value && previousValue !== value) {
			console.log('value:'+value);
			console.log('previousValue:'+previousValue);
		}else{
			console.log("else");
		}
		console.log('Componente Rendered');

	}, [previousValue, value]);
	return <FieldBase
		label={label}
		name={name}
		predefinedValue={() => {
			return predefinedValue;
		}}
		{...otherProps}
	>
		<UserDataFieldDDMFormFieldType
			name={name}
			onChange={(event) => {
				setCurrentValue(event.target.value);
				onChange(event);
			}}
			readOnly={readOnly}
			predefinedValue={() => {
				return predefinedValue;
			}}
			value={currentValue}
		/>
	</FieldBase>
}
export default Main;
