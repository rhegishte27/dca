import React from 'react';
import { DropdownMenu } from 'equisoft-design-ui-elements';
import { useAuthState } from '../../../page/authContext';
import HeaderMenu from '../../navigation/menu/headerMenu';
import { Caret } from '../caret';
import { HeaderContainer, HeaderElement, LogoEquisoft, MenusContainer } from './style';

const Header = () => {
    const { configuration, logout } = useAuthState();

    return (
        <HeaderContainer>
            <LogoEquisoft src="./dca-logo-black.jpg" alt="Equisoft/dca" data-testid="logo"/>
            <MenusContainer>
                <HeaderMenu />
                <HeaderElement>
                    <li>Test Customer</li>
                    <li>
                        <DropdownMenu
                            ButtonContent={(show: boolean) => (
                                <>
                                    {configuration.user && configuration.user.identifier}
                                    <Caret isExpand={show} />
                                </>
                            )}
                            list={[
                                { label: 'Logout', action: logout }
                            ]}
                        />
                    </li>
                </HeaderElement>
            </MenusContainer>
        </HeaderContainer>
    );
};

export default Header;
