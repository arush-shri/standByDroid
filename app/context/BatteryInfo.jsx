import { NativeModules } from 'react-native';

const { BatteryModule } = NativeModules;

const BatteryFlow = async () => {
    try {
        const batteryInfo = await BatteryModule.getBatteryInfo();
        const { voltage, current } = batteryInfo;

        if (voltage && current) {
            // Convert units from mV and µA to V and A
            const voltageInVolts = voltage / 1000;
            const currentInAmperes = current / 1000000;

            // Watts = Volts * Amps
            const wattage = voltageInVolts * currentInAmperes;
            
            return {wattage: wattage.toFixed(2), voltage: voltageInVolts, current: currentInAmperes};
        } else {
            console.log('Could not retrieve voltage or current.');
            return null;
        }
    } catch (error) {
        console.error('Error getting battery info:', error);
        return null;
    }
};

export const GetBatteryFlow = async () => {
    const flow = await BatteryFlow();
    if (flow) {
        return flow;
    }
};