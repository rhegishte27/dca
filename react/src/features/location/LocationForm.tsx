import { Select, TextInput } from 'equisoft-design-ui-elements';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import useCustomForm from '../../components/form/Form';
import CancelAndSaveButton from '../../components/general/button/CancelAndSaveButtons';
import { defaultLocationService, defaultLocationTypeService, defaultPlatformTypeService } from '../../lib/context';
import BaseEnum from '../../lib/domain/entities/BaseEnum';
import Location from '../../lib/domain/entities/Location';
import LocationType from '../../lib/domain/entities/LocationType';
import PlatformType from '../../lib/domain/entities/PlatformType';
import LocationService from '../../lib/services/LocationService';
import LocationTypeService from '../../lib/services/LocationTypeService';
import PlatformTypeService from '../../lib/services/PlatformTypeService';
import { useAuthState } from '../../page/authContext';
import { FormTitle, MediumForm, PanelBreak } from '../common/style';
import { useLocationSchema } from './useLocationSchema';

interface Props {
    locationService?: LocationService;
    locationTypeService?: LocationTypeService;
    platformTypeService?: PlatformTypeService;

    data?: Location;

    openList(): void;
}

const LocationForm: React.FC<Props> = ({
                                           locationService = defaultLocationService,
                                           locationTypeService = defaultLocationTypeService,
                                           platformTypeService = defaultPlatformTypeService,
                                           openList,
                                           data
                                       }) => {
    const { i18n } = useTranslation(['common', 'message', 'location']);
    const { updateUserSetting } = useAuthState();
    const { fields, validations } = useLocationSchema();

    const [locationTypeEnum, setLocationTypeEnum] = useState<LocationType[]>([]);
    const [platformTypeEnum, setPlatformTypeEnum] = useState<PlatformType[]>([]);

    const location: Location = data || new Location();

    const getInitialValues = (): any => {
        return {
            ...location.toPlainObj(),
            [fields.locationType.name]: location.locationType ? location.locationType.id : '',
            [fields.platformType.name]: location.platformType ? location.platformType.id : ''
        };
    };

    React.useEffect(() => {
        loadForm();
    }, []);

    const loadForm = async () => {
        setLocationTypeEnum(await locationTypeService.findAll());
        setPlatformTypeEnum(await platformTypeService.findAll());
    };

    const save = async (v: any) => {
        let locationToSave: Location = prepareToSave(v.values);

        if (locationToSave.id) {
            await locationService.update(locationToSave);
        } else {
            locationToSave = await locationService.add(locationToSave);
        }
        toast.success(i18n.t('message:success:save', { what: locationToSave.identifier }));
        updateUserSetting('Location', locationToSave);
    };

    const prepareToSave = (valuesToSave: any): Location => {
        const locationToSave: Location = new Location();
        locationToSave.applyData(valuesToSave);
        locationToSave.locationType =
            locationTypeEnum.find(o => o.id.toString() === String(valuesToSave[fields.locationType.name])) || new LocationType();
        locationToSave.platformType =
            platformTypeEnum.find(r => r.id.toString() === String(valuesToSave[fields.platformType.name]));
        return locationToSave;
    };

    const {
        values,
        errors,
        handleChange,
        handleBlur,
        handleMultipleChange,
        handleSubmit
    } = useCustomForm({
        initialValues: getInitialValues(),
        onSubmit: async (v: any) => {
            await save(v);
        },
        validations,
        redirectAfterSave: openList
    });

    const onCancelClick = () => {
        openList();
    };

    const isEditMode = (): boolean => {
        return values[fields.id.name];
    };

    const isLocationTypeFTP = (): boolean => {
        const locationType = values[fields.locationType.name];
        return locationType && locationType.toString() === LocationType.FTP.id.toString();
    };

    const locationTypeChangeHandler = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const input = e.target.value;
        if (input.toString() === LocationType.FTP.id) {
            handleMultipleChange([
                { name: fields.locationType.name, value: input },
                { name: fields.serverName.name, value: '' },
                { name: fields.userName.name, value: '' },
                { name: fields.password.name, value: '' },
                { name: fields.platformType.name, value: '' }
            ]);
        } else {
            handleMultipleChange([
                { name: fields.locationType.name, value: input },
                { name: fields.serverName.name, value: null },
                { name: fields.userName.name, value: null },
                { name: fields.password.name, value: null },
                { name: fields.platformType.name, value: null }
            ]);
        }
    };

    return (
        <>
            <MediumForm onSubmit={handleSubmit}>
                {/*this is the solution to prevent browser to auto complete password and username fields*/}
                <input type="password" style={{ 'display': 'none' }} autoComplete='new-password'/>

                <FormTitle>{i18n.t('location:titlePlural')}</FormTitle>
                <PanelBreak/>

                <TextInput
                    autoFocus={!isEditMode()}
                    disabled={isEditMode()}
                    label={fields.identifier.label}
                    name={fields.identifier.name}
                    value={values[fields.identifier.name]}
                    isValid={!errors[fields.identifier.name]}
                    validationErrorMessage={errors[fields.identifier.name]}
                    maxLength={fields.identifier.maxSize}
                    onChange={handleChange}
                    onBlur={handleBlur}
                />
                <br/>
                <Select
                    autoFocus={isEditMode()}
                    label={fields.locationType.label}
                    name={fields.locationType.name}
                    emptySelectText="---"
                    options={locationTypeEnum.map((g: LocationType) => ({
                        label: i18n.t(BaseEnum.getValue(LocationType, g.id).name),
                        value: g.id
                    }))}
                    value={values[fields.locationType.name]}
                    isValid={!errors[fields.locationType.name]}
                    validationErrorMessage={errors[fields.locationType.name]}
                    onChange={locationTypeChangeHandler}
                />
                <br/>

                <TextInput
                    label={fields.path.label}
                    name={fields.path.name}
                    value={values[fields.path.name]}
                    isValid={!errors[fields.path.name]}
                    validationErrorMessage={errors[fields.path.name]}
                    maxLength={fields.path.maxSize}
                    onChange={handleChange}
                    onBlur={handleBlur}
                />
                <br/>

                <div hidden={!isLocationTypeFTP()}>
                    <TextInput
                        label={fields.serverName.label}
                        name={fields.serverName.name}
                        value={values[fields.serverName.name]}
                        isValid={!errors[fields.serverName.name]}
                        validationErrorMessage={errors[fields.serverName.name]}
                        maxLength={fields.serverName.maxSize}
                        onChange={handleChange}
                        onBlur={handleBlur}
                    />
                    <br/>
                    <TextInput
                        label={fields.userName.label}
                        name={fields.userName.name}
                        value={values[fields.userName.name]}
                        isValid={!errors[fields.userName.name]}
                        validationErrorMessage={errors[fields.userName.name]}
                        maxLength={fields.userName.maxSize}
                        onChange={handleChange}
                        onBlur={handleBlur}
                    />
                    <br/>
                    <TextInput
                        type="password"
                        label={fields.password.label}
                        name={fields.password.name}
                        value={values[fields.password.name]}
                        isValid={!errors[fields.password.name]}
                        validationErrorMessage={errors[fields.password.name]}
                        maxLength={fields.password.maxSize}
                        onChange={handleChange}
                        onBlur={handleBlur}
                    />
                    <br/>
                    <Select
                        label={fields.platformType.label}
                        name={fields.platformType.name}
                        emptySelectText="---"
                        options={platformTypeEnum.map((g: PlatformType) => ({
                            label: i18n.t(BaseEnum.getValue(PlatformType, g.id).name),
                            value: g.id
                        }))}
                        value={values[fields.platformType.name]}
                        isValid={!errors[fields.platformType.name]}
                        validationErrorMessage={errors[fields.platformType.name]}
                        onChange={handleChange}
                    />
                    <br/>
                </div>

                <CancelAndSaveButton onCancelClick={onCancelClick}/>

            </MediumForm>
        </>
    );
};

export default LocationForm;
