class TimeSlot < ApplicationRecord
  has_and_belongs_to_many :exp_procedures
end
