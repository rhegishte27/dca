import { Loading } from 'equisoft-design-ui-elements';
import React from 'react';
import ErrorBoundary from '../general/error/errorBoundary';
import { PanelContent } from '../navigation/navbar/style';
import { useMainAreaContext } from './MainAreaProvider';

const OrganizationList = React.lazy(() => import('../../features/organization/OrganizationList'));

const OrganizationForm = React.lazy(() => import('../../features/organization/OrganizationForm'));

const UserList = React.lazy(() => import('../../features/user/UserList'));

const UserForm = React.lazy(() => import('../../features/user/UserForm'));

const SystemList = React.lazy(() => import('../../features/system/SystemList'));

const SystemForm = React.lazy(() => import('../../features/system/SystemForm'));

const ProjectList = React.lazy(() => import('../../features/project/ProjectList'));

const ProjectForm = React.lazy(() => import('../../features/project/ProjectForm'));

const SettingForm = React.lazy(() => import('../../features/setting/SettingForm'));

const LocationForm = React.lazy(() => import('../../features/location/LocationForm'));

const LocationList = React.lazy(() => import('../../features/location/LocationList'));

const DataObjectContainerForm = React.lazy(() => import('../../features/dataobject/container/DataObjectContainerForm'));

const DataObjectImportForm = React.lazy(() => import('../../features/dataobject/list/DataObjectImportForm'));

const DataObjectResultForm = React.lazy(() => import('../../features/dataobject/result/DataObjectResultForm'));

const DataObjectCreateForm = React.lazy(() => import('../../features/dataobject/create/DataObjectCreateForm'));

const DataObjectSourceForm = React.lazy(() => import('../../features/dataobject/source/DataObjectSourceForm'));

const EditorArea = () => {
    const {openMainArea, mainAreaData} = useMainAreaContext();

    const data = mainAreaData.data;

    return (
        <PanelContent>
            {(() => {
                switch (mainAreaData.type) {
                    case 'OrganizationList':
                        return <OrganizationList openForm={(organization) => {
                            openMainArea('OrganizationForm', organization);
                        }
                        }/>;
                    case 'OrganizationForm':
                        return <OrganizationForm data={data} openList={() => {
                            openMainArea('OrganizationList');
                        }
                        }/>;
                    case 'UserList':
                        return <UserList openForm={(user) => {
                            openMainArea('UserForm', user);
                        }
                        }/>;
                    case 'UserForm':
                        return <UserForm data={data} openList={() => {
                            openMainArea('UserList');
                        }
                        }/>;
                    case 'SystemList':
                        return <SystemList openForm={(system) => {
                            openMainArea('SystemForm', system);
                        }
                        }/>;
                    case 'SystemForm':
                        return <SystemForm data={data} openList={() => {
                            openMainArea('SystemList');
                        }
                        }/>;
                    case 'ProjectList':
                        return <ProjectList openForm={(project) => {
                            openMainArea('ProjectForm', project);
                        }
                        }/>;
                    case 'ProjectForm':
                        return <ProjectForm data={data} openList={() => {
                            openMainArea('ProjectList');
                        }
                        }/>;
                    case 'SettingForm':
                        return <SettingForm data={data} openMainArea={() => {
                            openMainArea('');
                        }
                        }/>;
                    case 'LocationForm':
                        return <LocationForm data={data} openList={() => {
                            openMainArea('LocationList');
                        }
                        }/>;
                    case 'LocationList':
                        return <LocationList openForm={(location) => {
                            openMainArea('LocationForm', location);
                        }
                        }/>;
                    case 'DataObjectContainerForm':
                        return <DataObjectContainerForm data={data} onCancelClick={() => {
                            openMainArea('');
                        }
                        }/>;
                    case 'DataObjectImportForm':
                        return <DataObjectImportForm data={data} onCancelClick={() => {
                            openMainArea('');
                        }
                        }/>;
                    case 'DataObjectResultForm':
                        return <DataObjectResultForm data={data} onCancelClick={() => {
                            openMainArea('');
                        }
                        }/>;
                    case 'DataObjectCreateForm':
                        return <DataObjectCreateForm data={data} onCancelClick={() => {
                            openMainArea('');
                        }
                        }/>;
                    case 'DataObjectSource':
                        return <DataObjectSourceForm data={data} openMainArea={() => {
                            openMainArea('');
                        }
                        }/>;
                    case '':
                        return <></>;
                }
            })()}
        </PanelContent>

    );
};

export default (props: any) => (
    <React.Suspense fallback={<Loading/>}>
        <ErrorBoundary>
            <EditorArea {...props} />
        </ErrorBoundary>
    </React.Suspense>
);
