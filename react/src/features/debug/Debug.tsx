import React from 'react'

interface DebugProps {
    values: any,
    errors: any,
    touched: any
}

const Debug: React.FC<DebugProps> = ({ values, errors, touched }) => (
    <div>
        <code>Values: {JSON.stringify(values)}</code>
        <br />
        <code>Errors: {JSON.stringify(errors)}</code>
        <br />
        <code>Touched: {JSON.stringify(touched)}</code>
        <br />
    </div>
)

export default Debug
