class TimeSlot < ApplicationRecord
  has_and_belongs_to_many :exp_procedures
  has_one :expert, :through => :exp_procedures
  has_many :procedures, :through => :exp_procedures
end
