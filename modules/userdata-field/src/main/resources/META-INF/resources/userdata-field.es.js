import React, {useEffect} from 'react';
import {FieldBase} from 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es';
import {useSyncValue} from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';
import {ClayInput} from '@clayui/form';

/**
 * UserData Input React Component
 */

const UserDataFieldDDMFormFieldType = ({name, onChange, predefinedValue, readOnly, value}) => (
	<ClayInput
		className="ddm-field-text"
		disabled={readOnly}
		name={name}
		onInput={onChange}
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

	useEffect(() => {
		// Código a ejecutar después de cada renderizado
		console.log('Componente renderizado o actualizado');
		// Puedes realizar operaciones o llamadas a funciones aquí
	});

	return <FieldBase
			label={label}
			name={name}
			predefinedValue={() => {
				console.log("predefinedValue: "+predefinedValue);
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
					console.log("predefinedValue: "+predefinedValue);
					return predefinedValue;
				}}
				value={currentValue}
			/>
		</FieldBase>
}


export default Main;
