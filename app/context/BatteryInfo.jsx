import { NativeModules } from 'react-native';

const { BatteryModule } = NativeModules;

const calculateChargingWattage = async () => {
  try {
    const batteryInfo = await BatteryModule.getBatteryInfo();
    const { voltage, current } = batteryInfo;

    if (voltage && current) {
      // Convert units from mV and µA to V and A
      const voltageInVolts = voltage / 1000;
      const currentInAmperes = current / 1000000;

      // Watts = Volts * Amps
      const wattage = voltageInVolts * currentInAmperes;
      
      return wattage.toFixed(2);
    } else {
      console.log('Could not retrieve voltage or current.');
      return null;
    }
  } catch (error) {
    console.error('Error getting battery info:', error);
    return null;
  }
};

export const GetWattage = async () => {
  const wattage = await calculateChargingWattage();
  if (wattage) {
    return wattage;
  }
};