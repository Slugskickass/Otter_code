import pandas as pd
import Otter_tools as ot
import matplotlib.pyplot as plt
# # intialise data of lists.
# data = {'Channel': ['L1', 'L2', 'L3', 'L4', 'L5', 'L6', 'L7', 'L8', 'C1', 'C2', 'C3', 'C4'],
#         'Period': [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
#         'On': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
#         'OFF': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
#         }
#
# # Create DataFrame
# df = pd.DataFrame(data)
#
# # Print the output.
# print(df)

otter = ot.Otter()
print((otter.data))

otter.setperiod(20)
print((otter.data))

otter.clear()
print((otter.data))

otter.print_to_console()
otter.build_simple_led(4)
otter.add_simple_camera(2)

print((otter.data))

data_hold = otter.convert_to_array()
plt.imshow(data_hold)
plt.show()
