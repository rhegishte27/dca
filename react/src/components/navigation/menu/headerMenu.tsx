import { useDialog } from 'equisoft-design-ui-elements';
import React from 'react';
import { EntityType } from '../../../features/common/EntityType';
import DataObject from '../../../lib/domain/entities/DataObject';
import Location from '../../../lib/domain/entities/Location';
import Organization from '../../../lib/domain/entities/Organization';
import Project from '../../../lib/domain/entities/Project';
import System from '../../../lib/domain/entities/System';
import User from '../../../lib/domain/entities/User';
import { useAuthState } from '../../../page/authContext';
import SetModalDialog from '../../general/dialog/setModal/SetModalDialog';
import Menu from './Menu';
import { Nav } from './style';
import { useFileMenu } from './useFileMenu';
import { useHelpMenu } from './useHelpMenu';
import { useLocationMenu } from './useLocationMenu';
import { useOrganizationMenu } from './useOrganizationMenu';
import { useProjectMenu } from './useProjectMenu';
import { useSystemMenu } from './useSystemMenu';
import { useUserMenu } from './useUserMenu';

interface DefaultItems {
    organization: Organization | undefined;
    project: Project | undefined;
    user: User | undefined;
    system: System | undefined;
    location: Location | undefined;
    dataObject: DataObject | undefined;
}

const HeaderMenu = () => {
    const { configuration } = useAuthState();
    const [show, toggle] = useDialog();
    const [dialogModalType, setDialogModalType] = React.useState<EntityType>('');
    const [defaultItems, setDefaultItems] = React.useState<DefaultItems>({
        organization: undefined,
        project: undefined,
        user: undefined,
        system: undefined,
        location: undefined,
        dataObject: undefined
    });

    React.useEffect(() => {
        const userSetting = configuration.userSetting;
        if (userSetting) {
            setDefaultItems({
                organization: userSetting.defaultOrganization,
                project: userSetting.defaultProject,
                user: userSetting.defaultUser,
                system: userSetting.defaultSystem,
                location: userSetting.defaultLocation,
                dataObject: userSetting.defaultDataObject
            });
        }
    }, [configuration]);

    const toggleModal = (modalType: EntityType) => {
        setDialogModalType(modalType);
        toggle();
    };

    const { getFileMenuTitle, getFileMenuItems } = useFileMenu();
    const { getHelpMenuTitle, getHelpMenuItems } = useHelpMenu();
    const { getOrganizationMenuTitle, getOrganizationMenuItems } = useOrganizationMenu(toggleModal);
    const { getUserMenuTitle, getUserMenuItems } = useUserMenu(toggleModal);
    const { getSystemMenuTitle, getSystemMenuItems } = useSystemMenu(toggleModal);
    const { getProjectMenuTitle, getProjectMenuItems } = useProjectMenu(toggleModal);
    const { getLocationMenuTitle, getLocationMenuItems } = useLocationMenu(toggleModal);

    return (
        <Nav>
            <ul>
                <Menu title={getFileMenuTitle()} tree={getFileMenuItems()}/>
                <Menu title={getLocationMenuTitle()} tree={getLocationMenuItems(defaultItems.location)}/>
                <Menu title={getProjectMenuTitle()} tree={getProjectMenuItems(defaultItems.project)}/>
                <Menu title={getSystemMenuTitle()} tree={getSystemMenuItems(defaultItems.system, defaultItems.dataObject)}/>
                <Menu title={getOrganizationMenuTitle()} tree={getOrganizationMenuItems(defaultItems.organization)}/>
                <Menu title={getUserMenuTitle()} tree={getUserMenuItems(defaultItems.user)}/>
                <Menu title={getHelpMenuTitle()} tree={getHelpMenuItems()}/>
            </ul>
            <SetModalDialog
                show={show}
                modalType={dialogModalType}
                toggle={toggle}
            />
        </Nav>
    );
};

export default HeaderMenu;
