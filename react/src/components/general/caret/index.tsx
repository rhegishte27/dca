import React from 'react';
import styled from 'styled-components';
import { ChevronDown } from 'react-feather';

interface CaretProps {
    isExpand: boolean;
}

const DropdownIcon = styled(ChevronDown)`
    height: 12px;
    width: 12px;
`;

const CaretSpan = styled.span<CaretProps>`
    color: ${({ theme }) => theme.colors.text.tertiary};
    display: block;
    margin: 0 10px;
    height: 12px;
    transition: all 0.25s;
    transform: ${(props) => (props.isExpand ? 'rotate(0deg)' : 'rotate(-180deg)')};
    width: 12px;
`;

export const Caret = ({ isExpand }: CaretProps) => (
    <CaretSpan isExpand={isExpand}>
        <DropdownIcon />
    </CaretSpan>
);
