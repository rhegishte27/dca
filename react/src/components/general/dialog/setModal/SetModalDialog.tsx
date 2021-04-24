import { Dialog, Select } from 'equisoft-design-ui-elements';
import { SelectOption } from 'equisoft-design-ui-elements/dist/components/forms/selects/select';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { EntityType } from '../../../../features/common/EntityType';
import { sortList } from '../../../../features/common/listUtil';
import {
    defaultDataObjectService,
    defaultLocationService,
    defaultOrganizationService,
    defaultProjectService,
    defaultSystemService,
    defaultUserService
} from '../../../../lib/context';
import DataObject from '../../../../lib/domain/entities/DataObject';
import Location from '../../../../lib/domain/entities/Location';
import Organization from '../../../../lib/domain/entities/Organization';
import Project from '../../../../lib/domain/entities/Project';
import System from '../../../../lib/domain/entities/System';
import User from '../../../../lib/domain/entities/User';
import DataObjectService from '../../../../lib/services/DataObjectService';
import LocationService from '../../../../lib/services/LocationService';
import OrganizationService from '../../../../lib/services/OrganizationService';
import ProjectService from '../../../../lib/services/ProjectService';
import SystemService from '../../../../lib/services/SystemService';
import UserService from '../../../../lib/services/UserService';
import { useAuthState } from '../../../../page/authContext';

interface DialogSetModalProps {
    show: boolean,
    modalType: EntityType,
    organizationService?: OrganizationService;
    userService?: UserService;
    projectService?: ProjectService;
    systemService?: SystemService;
    locationService?: LocationService;
    dataObjectService?: DataObjectService;

    toggle(): void;
}

const SetModalDialog: React.FC<DialogSetModalProps> = ({
                                                           show,
                                                           modalType,
                                                           organizationService = defaultOrganizationService,
                                                           userService = defaultUserService,
                                                           projectService = defaultProjectService,
                                                           systemService = defaultSystemService,
                                                           locationService = defaultLocationService,
                                                           dataObjectService = defaultDataObjectService,
                                                           toggle
                                                       }) => {
    const { i18n } = useTranslation(['common', 'message', 'project', 'organization', 'user', 'system', 'location', 'dataObject']);
    const { configuration, updateUserSetting } = useAuthState();
    const [dialogProps, setDialogProps] = useState({});
    const [title, setTitle] = useState<string>('');
    const [listItems, setListItems] = useState<any[]>([]);
    const [mapItems, setMapItems] = useState<Map<string, string>>(new Map());
    const [idItemSelected, setIdItemSelected] = useState<string>('');

    React.useEffect(() => {
        loadDialog(show);
    }, [show]);

    const loadDialog = async (isShow: boolean) => {
        if (isShow) {
            switch (modalType) {
                case 'Project':
                    const listProjects: Project[] = await projectService.findAll();
                    setListItems(listProjects);
                    setTitle(i18n.t('project:setProject'));
                    setMapItems(getMap(listProjects, o => o.id, o => o.identifier));
                    if (configuration.userSetting && configuration.userSetting.defaultProject) {
                        setIdItemSelected(configuration.userSetting.defaultProject.id);
                    } else {
                        setIdItemSelected('');
                    }
                    break;
                case 'Organization':
                    const listOrganizations: Organization[] = await organizationService.findAll();
                    setListItems(listOrganizations);
                    setTitle(i18n.t('organization:setOrganization'));
                    setMapItems(getMap(listOrganizations, o => o.id, o => o.name));
                    if (configuration.userSetting && configuration.userSetting.defaultOrganization) {
                        setIdItemSelected(configuration.userSetting.defaultOrganization.id);
                    } else {
                        setIdItemSelected('');
                    }
                    break;
                case 'User':
                    const listUsers: User[] = await userService.findAll();
                    setListItems(listUsers);
                    setTitle(i18n.t('user:setUser'));
                    setMapItems(getMap(listUsers, o => o.id, o => o.identifier));
                    if (configuration.userSetting && configuration.userSetting.defaultUser) {
                        setIdItemSelected(configuration.userSetting.defaultUser.id);
                    } else {
                        setIdItemSelected('');
                    }
                    break;
                case 'System':
                    const listSystem: System[] = await systemService.findAll();
                    setListItems(listSystem);
                    setTitle(i18n.t('system:setSystem'));
                    setMapItems(getMap(listSystem, o => o.id, o => o.identifier));
                    if (configuration.userSetting && configuration.userSetting.defaultSystem) {
                        setIdItemSelected(configuration.userSetting.defaultSystem.id);
                    } else {
                        setIdItemSelected('');
                    }
                break;
                case 'Location':
                    const listLocation: Location[] = await locationService.findAll();
                    setListItems(listLocation);
                    setTitle(i18n.t('location:setLocation'));
                    setMapItems(getMap(listLocation, o => o.id, o => o.identifier));
                    if (configuration.userSetting && configuration.userSetting.defaultLocation) {
                        setIdItemSelected(configuration.userSetting.defaultLocation.id);
                    } else {
                        setIdItemSelected('');
                    }
                    break;
                case 'DataObject':
                    const listDataObject: DataObject[] = await dataObjectService.findAll();
                    setListItems(listDataObject);
                    setTitle(i18n.t('dataObject:setDataObject'));
                    setMapItems(getMap(listDataObject, o => o.id, o => o.systemIdentifier + ' - ' + o.identifier));
                    if (configuration.userSetting && configuration.userSetting.defaultDataObject) {
                        setIdItemSelected(configuration.userSetting.defaultDataObject.id);
                    } else {
                        setIdItemSelected('');
                    }
                    break;
                case '':
                    break;
            }
        }
    };

    const onConfirm = (): void => {
        let defaultEntity: any;

        switch (modalType) {
            case 'Project':
                if (idItemSelected) {
                    defaultEntity = (listItems as Project[]).find(p => p.id.toString() === idItemSelected.toString());
                }
                break;
            case 'Organization':
                if (idItemSelected) {
                    defaultEntity = (listItems as Organization[]).find(p => p.id.toString() === idItemSelected.toString());
                }
                break;
            case 'User':
                if (idItemSelected) {
                    defaultEntity = (listItems as User[]).find(p => p.id.toString() === idItemSelected.toString());
                }
                break;
            case 'System':
                if (idItemSelected) {
                    defaultEntity = (listItems as System[]).find(p => p.id.toString() === idItemSelected.toString());
                }
                break;
            case 'Location':
                if (idItemSelected) {
                    defaultEntity = (listItems as Location[]).find(p => p.id.toString() === idItemSelected.toString());
                }
                break;
            case 'DataObject':
                if (idItemSelected) {
                    defaultEntity = (listItems as DataObject[]).find(p => p.id.toString() === idItemSelected.toString());
                }
                break;
            case '':
                break;
        }
        updateUserSetting(modalType, defaultEntity);
    };

    const getMap = function <T>(lstObject: T[], getId: (obj: T) => string, getIdentifier: (obj: T) => string): Map<string, string> {
        const map = new Map();
        const lstObjectSorted = sortList(lstObject, getIdentifier);
        for (const obj of lstObjectSorted) {
            map.set(getId(obj).toString(), getIdentifier(obj));
        }
        return map;
    };

    const getListOption = (map: Map<string, string>): SelectOption[] => {
        const listOption: SelectOption[] = [];
        for (const [key, value] of map) {
            listOption.push({ label: value, value: key });
        }
        return listOption;
    };

    const getDialogProps = () => {
        const baseProps = {
            show,
            onClose: closeDialog,
            title: title,
            confirmPanel: true,
            onConfirm: onConfirm,
            element:
                <>
                    <br/>
                    <Select
                        value={idItemSelected}
                        emptySelectText="---"
                        options={getListOption(mapItems)}
                        onChange={(e) => {
                            setIdItemSelected(e.target.value);
                        }}
                    />
                    <br/>
                </>,
            confirmMessage: i18n.t('common:button:set'),
            cancelMessage: i18n.t('common:button:cancel')
        };
        return { ...baseProps, ...dialogProps };
    };

    const closeDialog = () => {
        setDialogProps({});
        toggle();
    };


    return (
        <Dialog {...getDialogProps()} />
    );
};

export default SetModalDialog;