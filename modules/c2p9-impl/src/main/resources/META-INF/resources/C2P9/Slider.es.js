import React, {useEffect} from 'react';
import {useSyncValue} from 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es';


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
		// If the value has changed
		// find the collection of elements with the class name 'my-text'
		const collection = document.getElementsByClassName('ddm-form-field-repeatable-add-button');
		// turn the collection into an array
		const myElements = Array.from(collection);
		// loop through the collection of elements
		// and style each element
		myElements.forEach((element, index) => {
			if (index % 2 !== 0) {
				element.style.background = 'green';
				element.title = 'Duplicate Odds';
			}
		});
	});
	return ""
}
export default Main;

