import pandas as pd

# intialise data of lists.
data = {'Channel': ['L1', 'L2', 'L3', 'L4', 'L5', 'L6', 'L7', 'L8', 'C1', 'C2', 'C3', 'C4'],
        'Period': [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
        'On': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        'OFF': [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        }

# Create DataFrame
df = pd.DataFrame(data)

# Print the output.
print(df)