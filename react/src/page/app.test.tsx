import {render} from '@testing-library/react';
import React from 'react';
import App from "./index";

describe('<App/>', () => {
    test('renders without crashing ', () => {
        const { getByText } = render(<App />);
        expect(getByText('Loading ...')).toBeInTheDocument();
    });
});