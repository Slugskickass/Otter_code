import numpy as np
import pandas as pd
import serial
import sys
import os


class Otter(object):
    """
    Image stack object for holding and collating an NDR dataset
    Stack class is called with the filename as the constructor,
    the class automatically will run through the data obtaining
    the width, height, number of frames, location of the first reset,
    and the difference between resets.
    Useful functions:
    get_frame(frame_number) - returns the image data from a particular frame
    get_pixel_trace(start_frame_number, end_frame_number, x_pixel, y_pixel) - returns the time trace for a particular
        pixel between start and end
    """

# intialise data of lists.
    def __init__(self):
        data = {'Channel': ['L1', 'L2', 'L3', 'L4', 'L5', 'L6', 'L7', 'L8', 'C1', 'C2', 'C3', 'C4'],
        'Period': [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
        'On': [1, 2, 1, 1, 2, 3, 4, 5, 0, 0, 0, 0],
        'Off': [2, 5, 3, 1, 2, 4, 5, 6, 0, 0, 0, 0],
        }
        self.data = pd.DataFrame(data)

    def define_drive_camera(self, camera):
        self.drive_camera = camera
        print('')

    def clear(self):
        self.data.iloc[:, 1] = 0
        self.data.iloc[:, 2] = 0

    def build_simple_led(self, Leds):
        self.setperiod(Leds)
        for Position in range(Leds):
            self.data.iloc[Position, 2] = Position
            self.data.iloc[Position, 1] = Position+1

    def add_simple_camera(self, camera):
        camera_name = "C" + str(camera)
        indy = self.data.index[self.data['Channel'] == camera_name]

        self.data.ix[indy, 'On'] = int(0)
        self.data.ix[indy, 'Off'] = self.data['Period'].max()

    def setperiod(self, period):
        self.data['Period'] = period

    def write_to_serial(self):
        with serial.Serial('/dev/ttyS1', 19200, timeout=1) as ser:
            print('hi;')

    def write_to_file(self):
        print('hi')

    def get_max(self):
        maxValuesObj = self.data.max()
        print('Maximum value in each column : ')
        print(maxValuesObj)

    def print_to_console(self):
        shape = self.data.shape
        for Position in range(shape[0]):
            data_temp = self.data.iloc[Position, :]
            string_to_out = 'W' + ' ' + 'P' + ' ' + str(data_temp['Period']) +';'
            print(string_to_out)
            string_to_out = 'W' + ' ' + 'S' + ' ' + str(data_temp['On']) + ';'
            print(string_to_out)
            string_to_out = 'W' + ' ' + 'E' + ' ' + str(data_temp['Off']) + ';'
            print(string_to_out)

    def convert_to_array(self):
        shape = self.data.shape
        temp_array = np.zeros([shape[0], self.data['Period'].max()])
        for Position in range(shape[0]):
            data_temp = self.data.iloc[Position , :]
            temp_array[Position, data_temp['On']:data_temp['Off']] = 1
        return temp_array
